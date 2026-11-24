import com.orm.db.*;
import com.orm.dto.*;
import com.crud.*;
import java.sql.*;
public class TestCRUD
{
public static void main(String[] as)
{
Connection connection=null;
try
{
connection=DatabaseConnector.connect();
Department department=new Department();
department.setCode(10);
department.setName("ZZ Test");
System.out.println("Insert (expect 1) : "+ORMOperations.dbInsert(connection,department));
System.out.println("Insert again, duplicate key (expect -1) : "+ORMOperations.dbInsert(connection,department));
department.setName("ZZ Test 2");
System.out.println("Update (expect 1) : "+ORMOperations.dbUpdate(connection,department));
Department unknown=new Department();
unknown.setCode(999);
unknown.setName("Nobody");
System.out.println("Update unknown id (expect 0) : "+ORMOperations.dbUpdate(connection,unknown));
System.out.println("Delete unknown id (expect 0) : "+ORMOperations.dbDelete(connection,unknown));
System.out.println("Delete (expect 1) : "+ORMOperations.dbDelete(connection,department));
System.out.println("Delete again (expect 0) : "+ORMOperations.dbDelete(connection,department));
}catch(Exception exception)
{
exception.printStackTrace();
}finally
{
try
{
if(connection!=null) connection.close();
}catch(Exception exception)
{
exception.printStackTrace();
}
}
}
}