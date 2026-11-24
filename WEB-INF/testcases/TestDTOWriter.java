import com.orm.db.*;
import com.generator.*;
import java.util.*;
import java.io.*;
public class TestDTOWriter
{
public static void main(String args[]) throws Exception
{
TableColumn col1=new TableColumn();
col1.setName("id");
col1.setType("INT");

TableColumn col2=new TableColumn();
col2.setName("first_name");
col2.setType("VARCHAR");

TableColumn col3=new TableColumn();
col3.setName("is_active");
col3.setType("BOOLEAN");

List<TableColumn> columns=new ArrayList<TableColumn>();
columns.add(col1);
columns.add(col2);
columns.add(col3);

Table table=new Table();
table.setName("employee_details");
table.setTableColumns(columns);

List<Table> tables=new ArrayList<Table>();
tables.add(table);

Database database=new Database();
database.setTables(tables);

File outputFolder=new File("dto");
if(!outputFolder.exists()) outputFolder.mkdirs();

DTOWriter.generate(database,"com.test.dto",outputFolder);

System.out.println("Done. Check: "+outputFolder.getAbsolutePath());
}
}