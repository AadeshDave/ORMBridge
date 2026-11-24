import java.sql.*;
import com.orm.db.*;
import com.crud.*;
public class Test
{
public static void main(String[] args)
{
try(Connection connection=DatabaseConnector.connect("yourUser","yourPassword"))
{
ShpCustomer customer=new ShpCustomer();
customer.setCustomerName("Ravi");
System.out.println(ORMOperations.dbInsert(connection,customer));
}catch(Exception exception)
{
exception.printStackTrace();
}
}
}