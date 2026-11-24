package com.test.annotations;
import com.orm.annotation.PrimaryKey;
import com.orm.annotation.ForeignKey;
import com.orm.annotation.MaxLength;
import com.orm.annotation.Check;
import com.orm.annotation.Column;
public class EmployeeDetails implements java.io.Serializable,Comparable<EmployeeDetails>
{
@PrimaryKey
@Column(name="emp_id")
private int empId;
@Column(name="first_name")
@MaxLength(50)
private String firstName;
@Column(name="age")
@Check(constraint="age > 0")
private int age;
@ForeignKey(table="department",column="dept_id")
@Column(name="dept_id")
private int deptId;
public EmployeeDetails()
{
this.empId=0;
this.firstName="";
this.age=0;
this.deptId=0;
}
public EmployeeDetails(int empId,String firstName,int age,int deptId)
{
this.empId=empId;
this.firstName=firstName;
this.age=age;
this.deptId=deptId;
}
public void setEmpId(int empId)
{
this.empId=empId;
}
public void setFirstName(String firstName)
{
this.firstName=firstName;
}
public void setAge(int age)
{
this.age=age;
}
public void setDeptId(int deptId)
{
this.deptId=deptId;
}
public int getEmpId()
{
return this.empId;
}
public String getFirstName()
{
return this.firstName;
}
public int getAge()
{
return this.age;
}
public int getDeptId()
{
return this.deptId;
}
public boolean equals(Object object)
{
if(this==object) return true;
if(object==null || getClass()!=object.getClass()) return false;
EmployeeDetails other=(EmployeeDetails)object;
return this.empId==other.empId;
}
public int compareTo(EmployeeDetails object)
{
EmployeeDetails other=object;
return Integer.compare(this.empId,other.empId);
}
public int hashCode()
{
return java.util.Objects.hash(empId, firstName, age, deptId);
}
}
