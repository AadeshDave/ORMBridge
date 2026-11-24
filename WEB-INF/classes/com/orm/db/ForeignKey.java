package com.orm.db;
import java.util.*;
public class ForeignKey
{
private String parentChildTable;
private List<ForeignKeyMapping> foreignKeyMapping;
public void setParentChildTable(String parentChildTable)
{
this.parentChildTable=parentChildTable;
}
public String getParentChildTable()
{
return this.parentChildTable;
}
public void setForeignKeyMapping(List<ForeignKeyMapping> foreignKeyMapping)
{
this.foreignKeyMapping=foreignKeyMapping;
}
public List<ForeignKeyMapping> getForeignKeyMapping()
{
return this.foreignKeyMapping;
}

}