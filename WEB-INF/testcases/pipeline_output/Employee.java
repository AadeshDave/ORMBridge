package com.test.pipeline;
public class Employee implements java.io.Serializable,Comparable
{
private int id;
private String name;
private java.math.BigDecimal salary;
private boolean isActive;
public Employee()
{
this.id=0;
this.name="";
this.salary=new java.math.BigDecimal("0.0");
this.isActive=false;
}
public Employee(int id,String name,java.math.BigDecimal salary,boolean isActive)
{
this.id=id;
this.name=name;
this.salary=salary;
this.isActive=isActive;
}
public void setId(int id)
{
this.id=id;
}
public void setName(String name)
{
this.name=name;
}
public void setSalary(java.math.BigDecimal salary)
{
this.salary=salary;
}
public void setIsActive(boolean isActive)
{
this.isActive=isActive;
}
public int getId()
{
return this.id;
}
public String getName()
{
return this.name;
}
public java.math.BigDecimal getSalary()
{
return this.salary;
}
public boolean getIsActive()
{
return this.isActive;
}
public boolean equals(Object object)
{
if(this==object) return true;
if(object==null || getClass()!=object.getClass()) return false;
Employee other=(Employee)object;
return this.id==other.id && this.name.equals(other.name) && this.salary.equals(other.salary) && this.isActive==other.isActive;
}
public int compareTo(Object object)
{
Employee other=(Employee)object;
return this.id-other.id;
}
public int hashCode()
{
return java.util.Objects.hash(id, name, salary, isActive);
}
}
