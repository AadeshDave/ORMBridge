package com.crud;
import com.orm.db.*;
import java.sql.*;
import java.util.*;
public class ORMOperations
{
public static int dbInsert(Connection connection,Object object)
{
try
{
ORMMapping ormMapping;
if(object instanceof ORMMapping)
{
ormMapping=(ORMMapping)object;
}
else
{
ormMapping=new ORMMapping(object);
}
StringBuffer columnBuffer=new StringBuffer();
StringBuffer placeHolderBuffer=new StringBuffer();
List<Object> parameters=new ArrayList<>();
for(int i=0;i<ormMapping.columnNames.size();++i)
{
String nameOfField=ormMapping.columnNames.get(i);
if(ormMapping.autoValues.contains(nameOfField)) continue;
if(columnBuffer.length()>0)
{
columnBuffer.append(",");
placeHolderBuffer.append(",");
}
columnBuffer.append("`"+nameOfField+"`");
placeHolderBuffer.append("?");
parameters.add(ormMapping.values.get(i));
}
String sqlStatement="Insert into `"+ormMapping.tableName+"` ("+columnBuffer+") values ("+placeHolderBuffer+")";
try(PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement))
{
for(int t=0;t<parameters.size();++t)
{
preparedStatement.setObject(t+1,parameters.get(t));
}
return preparedStatement.executeUpdate();
}
}catch(Exception exception)
{
exception.printStackTrace();
return -1;
}
}
public static int dbDelete(Connection connection,Object object)
{
try
{
ORMMapping ormMapping;
if(object instanceof ORMMapping)
{
ormMapping=(ORMMapping)object;
}
else
{
ormMapping=new ORMMapping(object);
}
if(ormMapping.keyColumnNames.isEmpty()) return -1;
for(int i=0;i<ormMapping.keyValues.size();++i)
{
if(ormMapping.keyValues.get(i)==null) return -1;
}
StringBuffer whereBuffer=new StringBuffer();
List<Object> parameters=new ArrayList<>();
for(int j=0;j<ormMapping.keyColumnNames.size();++j)
{
if(j>0) whereBuffer.append(" and ");
whereBuffer.append("`"+ormMapping.keyColumnNames.get(j)+"`=?");
parameters.add(ormMapping.keyValues.get(j));
}
String sqlStatement="Delete from `"+ormMapping.tableName+"` where "+whereBuffer;
try(PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement))
{
for(int k=0;k<parameters.size();++k)
{
preparedStatement.setObject(k+1,parameters.get(k));
}
return preparedStatement.executeUpdate();
}
}catch(Exception exception)
{
exception.printStackTrace();
return -1;
}
}
public static int dbUpdate(Connection connection,Object object)
{
try
{
ORMMapping ormMapping;
if(object instanceof ORMMapping)
{
ormMapping=(ORMMapping)object;
}
else
{
ormMapping=new ORMMapping(object);
}
if(ormMapping.keyColumnNames.isEmpty()) return -1;
for(int j=0;j<ormMapping.keyValues.size();++j)
{
if(ormMapping.keyValues.get(j)==null) return -1;
}
StringBuffer setBuffer=new StringBuffer();
StringBuffer whereBuffer=new StringBuffer();
List<Object> parameters=new ArrayList<>();
for(int i=0;i<ormMapping.columnNames.size();++i)
{
String name=ormMapping.columnNames.get(i);
if(ormMapping.keyColumnNames.contains(name)) continue;
if(setBuffer.length()>0)
{
setBuffer.append(",");
}
setBuffer.append("`"+name+"`=?");
parameters.add(ormMapping.values.get(i));
}
if(setBuffer.length()==0) return -1;
for(int k=0;k<ormMapping.keyColumnNames.size();++k)
{
if(k>0)
{
whereBuffer.append(" and ");
}
whereBuffer.append("`"+ormMapping.keyColumnNames.get(k)+"`=?");
parameters.add(ormMapping.keyValues.get(k));
}
String sqlStatement="Update `"+ormMapping.tableName+"` set "+setBuffer+" where "+whereBuffer;
try(PreparedStatement preparedStatement=connection.prepareStatement(sqlStatement))
{
for(int t=0;t<parameters.size();++t)
{
preparedStatement.setObject(t+1,parameters.get(t));
}
return preparedStatement.executeUpdate();
}
}catch(Exception exception)
{
exception.printStackTrace();
return -1;
}
}
public static List<String> dbTables(Connection connection)
{
List<String> tables;
try(ResultSet resultSet=connection.getMetaData().getTables(connection.getCatalog(),null,"%",new String[]{"TABLE"}))
{
tables=new ArrayList<String>();
while(resultSet.next())
{
tables.add(resultSet.getString("TABLE_NAME"));
}
Collections.sort(tables);
return tables;
}catch(SQLException sqlException)
{
sqlException.printStackTrace();
return null;
}
}
public static Map<String,Object> dbRows(Connection connection,String table)
{
try
{
Set<String> keys=new HashSet<>();
try(ResultSet resultSet=connection.getMetaData().getPrimaryKeys(connection.getCatalog(),null,table))
{
while(resultSet.next())
{
keys.add(resultSet.getString("COLUMN_NAME"));
}
}
List<Map<String,Object>> columns=new ArrayList<>();
List<List<String>> rows=new ArrayList<>();
try(PreparedStatement preparedStatement=connection.prepareStatement("Select * from `"+table.replace("`","``")+"`"))
{
preparedStatement.setMaxRows(500);
try(ResultSet resultSet=preparedStatement.executeQuery())
{
ResultSetMetaData information=resultSet.getMetaData();
int count=information.getColumnCount();
for(int i=1;i<=count;++i)
{
Map<String,Object> column=new LinkedHashMap<>();
String name=information.getColumnName(i);
String type=information.getColumnTypeName(i);
boolean nullable=(information.isNullable(i)!=ResultSetMetaData.columnNoNulls);
boolean autoIncrement=information.isAutoIncrement(i);
boolean primaryKey=keys.contains(name);
column.put("name",name);
column.put("type",type);
column.put("nullable",nullable);
column.put("autoIncrement",autoIncrement);
column.put("primaryKey",primaryKey);
columns.add(column);
}
while(resultSet.next())
{
List<String> row=new ArrayList<>();
for(int j=1;j<=count;++j)
{
String r=resultSet.getString(j);
row.add(r);
}
rows.add(row);
}
}
}
Map<String,Object> result=new LinkedHashMap<>();
result.put("columns",columns);
result.put("rows",rows);
result.put("truncated",rows.size()==500);
result.put("readOnly",keys.isEmpty());
return result;
}catch(SQLException sqlException)
{
sqlException.printStackTrace();
return null;
}
}
}