import com.generator.*;
import com.orm.db.*;
import java.io.*;
public class TestPipeline
{
public static void main(String args[]) throws Exception
{
String sql="CREATE TABLE employee (id INT PRIMARY KEY, name VARCHAR(50), salary DECIMAL(10,2), is_active BOOLEAN);";

Database database=SQLScriptParser.parse(sql);

File outputFolder=new File("pipeline_output");
if(!outputFolder.exists()) outputFolder.mkdirs();

DTOWriter.generate(database,"com.test.pipeline",outputFolder);

System.out.println("Done. Check: "+outputFolder.getAbsolutePath());
}
}