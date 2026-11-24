package com.orm.db;
public class TableColumn
{
private String name;
private int width;
private int precision;
private String type;
private boolean hasPrimaryKeyConstraintApplied;
private boolean hasNotNullConstraintApplied;
private boolean hasUniqueConstraintApplied;
private boolean isAutoIncrementable;
private String defaultValue;
String checkConstraint;
public TableColumn()
{
this.name="";
this.width=0;
this.precision=0;
this.type="";
this.hasPrimaryKeyConstraintApplied=false;
this.hasNotNullConstraintApplied=false;
this.hasUniqueConstraintApplied=false;
this.isAutoIncrementable=false;
this.defaultValue="";
this.checkConstraint="";
}
public void setName(String name)
{
this.name=name;
}
public String getName()
{
return this.name;
}
public void setType(String type)
{
this.type=type;
}
public String getType()
{
return this.type;
}
public void setDefaultValue(String defaultValue)
{
this.defaultValue=defaultValue;
}
public String get()
{
return this.defaultValue;
}
public void setWidth(int width)
{
this.width=width;
}
public int getWidth()
{
return this.width;
}
public void setPrecision(int precision)
{
this.precision=precision;
}
public int getPrecision()
{
return this.precision;
}
public void setHasPrimaryKeyConstraintApplied(boolean hasPrimaryKeyConstraintApplied)
{
this.hasPrimaryKeyConstraintApplied=hasPrimaryKeyConstraintApplied;
}
public boolean getHasPrimaryKeyConstrientApplied()
{
return this.hasPrimaryKeyConstraintApplied;
}
public void setHasNotNullConstraintApplied(boolean hasNotNullConstraintApplied)
{
this.hasNotNullConstraintApplied=hasNotNullConstraintApplied;
}
public boolean getHasNotNullConstrientApplied()
{
return this.hasNotNullConstraintApplied;
}
public void setIsAutoIncrementable(boolean isAutoIncrementable)
{
this.isAutoIncrementable=isAutoIncrementable;
}
public boolean getIsAutoIncrementable()
{
return this.isAutoIncrementable;
}
public void setHasUniqueConstraintApplied(boolean hasUniqueConstraintApplied)
{
this.hasUniqueConstraintApplied=hasUniqueConstraintApplied;
}
public boolean getUniqueConstrientApplied()
{
return this.hasUniqueConstraintApplied;
}
public void setCheckConstraint(String checkConstraint)
{
this.checkConstraint=checkConstraint;
}
public String getCheckConstraint()
{
return this.checkConstraint;
}
}