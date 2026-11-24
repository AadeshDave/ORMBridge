import com.orm.db.*;
import com.test.annotations.*;
public class TestORMMapping
{
public static void main(String as[])
{
try
{
Department department=new Department();
department.setDeptId(10);
department.setDeptName("Sales");
ORMMapping ormMapping=new ORMMapping(department);
System.out.println(ormMapping.tableName);
System.out.println(ormMapping.columnNames);
System.out.println(ormMapping.values);
System.out.println(ormMapping.keyColumnNames);
System.out.println(ormMapping.keyValues);
}catch(Exception exception)
{
System.out.println(exception.getMessage());
}
}
}