package com.orm.db;
import java.util.*;
public class Database
{
private List<Table> tables;
private List<java.lang.String> errors;
private List<String> createStatements;
public void setTables(List<Table> tables)
{
this.tables=tables;
}
public List<Table> getTables()
{
return this.tables;
}
public void setErrors(List<java.lang.String> errors)
{
this.errors=errors;
}
public List<java.lang.String> getErrors()
{
return this.errors;
}
public void setCreateStatements(List<String> createStatements)
{
this.createStatements=createStatements;
}
public List<String> getCreateStatements()
{
return this.createStatements;
}
}