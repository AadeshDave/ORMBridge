package com.generator;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.create.table.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.create.table.ForeignKeyIndex;
import java.util.*;
import java.sql.*;
import com.orm.db.*;
public class  SQLScriptParser
{
private static Table convert(CreateTable createTable)
{
String tableName=clean(createTable.getTable().getName());
List<TableColumn> tableColumns=new ArrayList<>();
for(ColumnDefinition columnDefinition:createTable.getColumnDefinitions())
{
String columnName=clean(columnDefinition.getColumnName());
String columnType=columnDefinition.getColDataType().getDataType();
TableColumn tableColumn=new TableColumn();
tableColumn.setName(columnName);
tableColumn.setType(columnType.toUpperCase());
List<String> arguments=columnDefinition.getColDataType().getArgumentsStringList();
if(arguments!=null && arguments.size()>=1)
{
int argument1=Integer.parseInt(arguments.get(0));
tableColumn.setWidth(argument1);
}
if(arguments!=null && arguments.size()>=2)
{
int argument2=Integer.parseInt(arguments.get(1));
tableColumn.setPrecision(argument2);
}
List<String> specs=columnDefinition.getColumnSpecs();
if(specs!=null)
{
String specsString=String.join(" ",specs).toUpperCase();
if(specsString.contains("PRIMARY KEY")) tableColumn.setHasPrimaryKeyConstraintApplied(true);
if(specsString.contains("AUTO_INCREMENT")) tableColumn.setIsAutoIncrementable(true);
String originalText=String.join(" ",specs);
int checkPosition=originalText.toUpperCase().indexOf("CHECK");
if(checkPosition!=-1)
{
int open=originalText.indexOf("(",checkPosition);
int close=originalText.lastIndexOf(")");
if(open!=-1 && close>open) tableColumn.setCheckConstraint(originalText.substring(open+1,close).trim());
}
}
tableColumns.add(tableColumn);
}
if(createTable.getIndexes()!=null)
{
for(Index index:createTable.getIndexes())
{
String pk="PRIMARY KEY";
if(pk.equalsIgnoreCase(index.getType()))
{
for(String colName:index.getColumnsNames())
{
for(TableColumn tc:tableColumns)
{
if(tc.getName().equalsIgnoreCase(clean(colName))) tc.setHasPrimaryKeyConstraintApplied(true);
}
}
}
}
}
List<ForeignKey> foreignKeys=new ArrayList<>();
if(createTable.getIndexes()!=null)
{
for(Index index:createTable.getIndexes())
{
if(index instanceof ForeignKeyIndex)
{
ForeignKeyIndex fk=(ForeignKeyIndex)index;
ForeignKey foreignKey=new ForeignKey();
foreignKey.setParentChildTable(clean(fk.getTable().getName()));
List<String> childColumns=fk.getColumnsNames();
List<String> parentColumns=fk.getReferencedColumnNames();
List<ForeignKeyMapping> mappings=new ArrayList<>();
for(int e=0;e<childColumns.size();++e)
{
ForeignKeyMapping foreignKeyMapping=new ForeignKeyMapping();
foreignKeyMapping.setChildTableColumnName(clean(childColumns.get(e)));
foreignKeyMapping.setParentTableColumnName(clean(parentColumns.get(e)));
mappings.add(foreignKeyMapping);
}
foreignKey.setForeignKeyMapping(mappings);
foreignKeys.add(foreignKey);
}
}
}

Table table=new Table();
table.setName(tableName);
table.setTableColumns(tableColumns);
table.setForeignKey(foreignKeys);
return table;
}
public static Database parse(String script)
{
List<String> createStatements=new ArrayList<>();
List<Table> tables=new ArrayList<>();
List<String> errorMessages=new ArrayList<>();
try
{
Statements statements=CCJSqlParserUtil.parseStatements(script);
for(net.sf.jsqlparser.statement.Statement statement:statements.getStatements())
{
if(statement instanceof CreateTable)
{
CreateTable createTable=(CreateTable)statement;
if(createTable.getColumnDefinitions().isEmpty())
{
errorMessages.add("Table '"+createTable+"' has no columns");
continue;
}
createStatements.add(statement.toString());
Table table=convert((CreateTable)statement);
tables.add(table);
}
}
}catch(Exception exception)
{
errorMessages.add("SQL error :"+exception.getMessage());
}
Database databases=new Database();
databases.setTables(tables);
databases.setErrors(errorMessages);
databases.setCreateStatements(createStatements);
return databases;
}
private static Table validateThroughMySQL(String createTableSQL,Connection connection) throws Exception
{
CreateTable createTable=(CreateTable)CCJSqlParserUtil.parse(createTableSQL);
Table table=convert(createTable);
String tableName=clean(createTable.getTable().getName());
if(createTable.getIndexes()!=null)
{
List<Index> keepIt=new ArrayList<>();
for(Index index:createTable.getIndexes())
{
if(!(index instanceof ForeignKeyIndex)) keepIt.add(index);
}
createTable.setIndexes(keepIt);
}
String tempSQL=createTable.toString().replaceFirst("(?i)^CREATE\\s+TABLE","CREATE TEMPORARY TABLE");
String dropStatement="DROP TEMPORARY TABLE IF EXISTS "+tableName;
try(java.sql.Statement statement=connection.createStatement())
{
statement.execute(tempSQL);
statement.execute(dropStatement);
}
return table;
}
public static Database parseWithMySQL(String script,Connection connection)
{
List<String> createStatements=new ArrayList<>();
List<Table> tables=new ArrayList<>();
List<String> errorMessages=new ArrayList<>();
try
{
Statements statements=CCJSqlParserUtil.parseStatements(script);
for(net.sf.jsqlparser.statement.Statement statement:statements.getStatements())
{
String tableName="";
if(statement instanceof CreateTable) tableName=clean(((CreateTable)statement).getTable().getName());
try
{
if(statement instanceof CreateTable)
{
String tableSQL=statement.toString();
Table table=(validateThroughMySQL(tableSQL,connection));
tables.add(table);
createStatements.add(statement.toString());
}
}catch(Exception exception)
{
errorMessages.add("Table '"+tableName+"' is not valid SQL. Check the columns names types and syntax.");
}
}
}catch(Exception exception)
{
errorMessages.add("Error: "+exception.getMessage());
}
Database database=new Database();
database.setTables(tables);
database.setErrors(errorMessages);
database.setCreateStatements(createStatements);
return database;
}
private static String clean(String id)
{
if(id==null) return id;
id=id.trim();
int fullStop=id.lastIndexOf('.');
if(fullStop!=-1) id=id.substring(fullStop+1);
if(id.length()>0)
{
char first=id.charAt(0);
char last=id.charAt(id.length()-1);
if((first=='`' && last=='`') || (first=='"' && last=='"') || (first=='[' && last==']')) id=id.substring(1,id.length()-1);
}
return id;
}
}