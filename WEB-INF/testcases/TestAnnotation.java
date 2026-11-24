import com.generator.*;
import com.orm.db.*;
import java.io.*;

public class TestAnnotation
{
public static void main(String args[]) throws Exception
{
String sql=
"CREATE TABLE department (dept_id INT PRIMARY KEY, dept_name VARCHAR(50));"+
"CREATE TABLE employee_details (emp_id INT PRIMARY KEY, first_name VARCHAR(50), age INT CHECK (age > 0), dept_id INT, FOREIGN KEY (dept_id) REFERENCES department(dept_id));"+
"CREATE TABLE order_item (order_id INT, item_id INT, quantity INT CHECK (quantity >= 1), PRIMARY KEY (order_id, item_id));";Database database=SQLScriptParser.parse(sql);

File outputFolder=new File("annotation_output");
if(!outputFolder.exists()) outputFolder.mkdirs();

DTOWriter.generate(database,"com.test.annotations",outputFolder);

System.out.println("Done. Check: "+outputFolder.getAbsolutePath());
for(String error:database.getErrors())
{
System.out.println("ERROR: "+error);
}
}
}