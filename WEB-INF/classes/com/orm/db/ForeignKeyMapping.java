package com.orm.db;
public class ForeignKeyMapping
{
private String childTableColumnName;
private String parentTableColumnName;
public void setChildTableColumnName(String childTableColumnName)
{
this.childTableColumnName=childTableColumnName;
}
public String getChildTableColumnName()
{
return this.childTableColumnName;
}
public void setParentTableColumnName(String parentTableColumnName)
{
this.parentTableColumnName=parentTableColumnName;
}
public String getParentTableColumnName()
{
return this.parentTableColumnName;
}
}