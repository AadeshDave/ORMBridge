package com.orm.db;
import java.util.*;
public class Table
{
private String name;
private List<TableColumn> tableColumns;
private List<ForeignKey> foreignKey;
public Table()
{
this.name="";
this.tableColumns=null;
this.foreignKey=null;
}
public void setName(String name)
{
this.name=name;
}
public String getName()
{
return this.name;
}
public void setTableColumns(List<TableColumn> tableColumns)
{
this.tableColumns=tableColumns;
}
public List<TableColumn> getTableColumns()
{
return this.tableColumns;
}
public void setForeignKey(List<ForeignKey> foreignKey)
{
this.foreignKey=foreignKey;
}
public List<ForeignKey> getForeignKey()
{
return this.foreignKey;
}
}