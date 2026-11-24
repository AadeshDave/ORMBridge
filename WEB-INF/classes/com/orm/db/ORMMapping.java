package com.orm.db;
import java.util.*;
import java.lang.reflect.*;
import com.orm.annotation.*;
import java.sql.*;
public class ORMMapping
{
public String tableName;
public List<String> columnNames=new ArrayList<>();
public List<Object> values=new ArrayList<>();
public List<String> keyColumnNames=new ArrayList<>();
public List<Object> keyValues=new ArrayList<>();
public List<String> autoValues=new ArrayList<>();
public int keyCount;
public ORMMapping(Connection connection,String table,Map<String,String> data) throws Exception
{
this.tableName=table;
Set<String> keys=new HashSet<>();
try(ResultSet keyResult=connection.getMetaData().getPrimaryKeys(connection.getCatalog(),null,table))
{
while(keyResult.next())
{
keys.add(keyResult.getString("COLUMN_NAME"));
}
}
keyCount=keys.size();
try(PreparedStatement preparedStatement=connection.prepareStatement("Select * from `"+table.replace("`","``")+"` where 1=0");ResultSet resultSet=preparedStatement.executeQuery())
{
ResultSetMetaData information=resultSet.getMetaData();
for(int i=1;i<=information.getColumnCount();i++)
{
String name=information.getColumnName(i);
if(!data.containsKey(name)) continue;
String value=data.get(name);
if(value!=null && value.trim().isEmpty() && information.isNullable(i)!=ResultSetMetaData.columnNoNulls) value=null;
columnNames.add(name);
values.add(value);
if(keys.contains(name))
{
keyColumnNames.add(name);
keyValues.add(value);
}
}
}
}
public ORMMapping(Object object) throws Exception
{
Class<?> c=object.getClass();
tableName=c.getSimpleName();
List<String> compositeKeyNames=new ArrayList<>();
CompositeKey compositeKey;
compositeKey=c.getAnnotation(CompositeKey.class);
if(compositeKey!=null)
{
String[] names=compositeKey.columns();
for(int i=0;i<names.length;++i)
{
compositeKeyNames.add(names[i]);
}
}
Field[] fields=c.getDeclaredFields();
for(Field f:fields)
{
f.setAccessible(true);
if(Modifier.isStatic(f.getModifiers())) continue;
Column column=f.getAnnotation(Column.class);
String columnName=(column!=null)?column.name():f.getName();
Object value=f.get(object);
columnNames.add(columnName);
values.add(value);
if(f.getAnnotation(PrimaryKey.class)!=null)
{
keyColumnNames.add(columnName);
keyValues.add(value);
}
if(f.getAnnotation(Auto.class)!=null) autoValues.add(columnName);
}
for(String keyName:compositeKeyNames)
{
int index=columnNames.indexOf(keyName);
if(index!=-1 && !keyColumnNames.contains(keyName))
{
keyColumnNames.add(keyName);
keyValues.add(values.get(index));
}
}
}
}