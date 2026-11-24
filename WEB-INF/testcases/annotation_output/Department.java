package com.orm.dto;
import com.orm.annotation.PrimaryKey;
import com.orm.annotation.MaxLength;
import com.orm.annotation.Column;
public class Department implements java.io.Serializable,Comparable<Department>
{
@PrimaryKey
@Column(name="code")
private int code;
@Column(name="name")
@MaxLength(50)
private String name;
public Department()
{
this.code=0;
this.name="";
}
public Department(int code,String name)
{
this.code=code;
this.name=name;
}
public void setCode(int code)
{
this.code=code;
}
public void setName(String name)
{
this.name=name;
}
public int getCode()
{
return this.code;
}
public String getName()
{
return this.name;
}
public boolean equals(Object object)
{
if(this==object) return true;
if(object==null || getClass()!=object.getClass()) return false;
Department other=(Department)object;
return this.code==other.code;
}
public int compareTo(Department object)
{
Department other=object;
return Integer.compare(this.code,other.code);
}
public int hashCode()
{
return java.util.Objects.hash(code);
}
}
