import com.generator.*;
import com.orm.db.*;
import java.util.*;
public class TestParser
{
public static void main(String args[]) throws Exception
{
String sql="CREATE TABLE employee (id INT PRIMARY KEY, name VARCHAR(50), salary DECIMAL(10,2));";
Database database=SQLScriptParser.parse(sql);
List<Table> tables=database.getTables();
System.out.println("Tables found: "+tables.size());
for(Table t:tables)
{
System.out.println("Table: "+t.getName());
for(TableColumn tc:t.getTableColumns())
{
System.out.println("  Column: "+tc.getName()+" Type: "+tc.getType());
}
}
}
}