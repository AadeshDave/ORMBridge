package com.servlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.*;
import java.sql.*;
import com.orm.db.*;
import com.generator.*;
import java.nio.file.*;
import java.util.zip.*;
public class SQLToJavaServlet extends HttpServlet
{
public void doPost(HttpServletRequest request,HttpServletResponse response)
{
try
{
String sqlScript=request.getParameter("sqlScript");
sqlScript=(sqlScript==null)?"":sqlScript.trim();
System.out.println("SQL script : "+sqlScript);
String packageName=request.getParameter("packageName");
packageName=(packageName==null)?"":packageName.trim();
System.out.println("Package name : "+packageName);
String action=request.getParameter("action");
action=(action==null)?"":action.trim();
System.out.println("Action : "+action);

String validate=request.getParameter("validateWithMySQL");
validate=(validate==null)?"":validate.trim();
boolean validation="true".equalsIgnoreCase(validate);
System.out.println("Validation : "+validation);

boolean createTablesOnRequest="true".equals(request.getParameter("createTables"));
String dbUser=null;
String dbPassword=null;
if(createTablesOnRequest)
{
HttpSession session=request.getSession(false);
if(session==null || session.getAttribute("dbUser")==null)
{
response.setStatus(401);
response.setContentType("text/plain");
response.getWriter().write("Plese log in again");
return;
}
dbUser=(String)session.getAttribute("dbUser");
dbPassword=(String)session.getAttribute("dbPassword");
}
if(sqlScript.isEmpty() || packageName.isEmpty()) return;
Database database=null;
if(validation)
{
Connection connection=null;
try
{
connection=DatabaseConnector.connect();
database=SQLScriptParser.parseWithMySQL(sqlScript,connection);
}
catch(Exception exception)
{
exception.printStackTrace();
response.setStatus(500);
response.setContentType("text/plain");
response.getWriter().write("Validation is not available yet ! try after some time");
return;
}
finally
{
if(connection!=null) connection.close();
}
}
else
{
database=SQLScriptParser.parse(sqlScript);
}
if(database.getTables().isEmpty() && !database.getErrors().isEmpty())
{
response.setStatus(500);
response.setContentType("text/plain");
response.getWriter().write(String.join("\n",database.getErrors()));
return;
}
System.out.println("Errors : "+database.getErrors());
Path tempararyDirectoryPath=Files.createTempDirectory("sqltojava_");
File tempararyDirectory=tempararyDirectoryPath.toFile();
DTOWriter.generate(database,packageName,tempararyDirectory);
// code to zip file
String downloadZipIf="download";
if(downloadZipIf.equals(action))
{
File zipFile=File.createTempFile("output_",".zip");
try(FileOutputStream fileOutputStream=new FileOutputStream(zipFile); ZipOutputStream zipOutputStream=new ZipOutputStream(fileOutputStream))
{
File[] filesToZip=tempararyDirectory.listFiles();
if(filesToZip!=null)
{
for(File file:filesToZip)
{
ZipEntry zipEntry=new ZipEntry(file.getName());
zipOutputStream.putNextEntry(zipEntry);
FileInputStream fileInputStream=new FileInputStream(file);
byte[] buffer=new byte[1024];
int length;
while((length=fileInputStream.read(buffer))>0)
{
zipOutputStream.write(buffer,0,length);
}
fileInputStream.close();
zipOutputStream.closeEntry();
}
}
}
response.setContentType("application/zip");
response.setHeader("Content-Disposition","attachment; filename=generated-dto.zip");
FileInputStream zipInputStream=new FileInputStream(zipFile);
OutputStream responseOutputStream=response.getOutputStream();
byte[] buffer=new byte[1024];
int length;
while((length=zipInputStream.read(buffer))>0)
{
responseOutputStream.write(buffer,0,length);
}
zipInputStream.close();
responseOutputStream.flush();
}
else
{
List<String> tableResults=new ArrayList<>();
if(createTablesOnRequest)
{
if(!database.getErrors().isEmpty())
{
tableResults.add("Tables were not created because the script has errors.");
}
else
{
List<String> names=new ArrayList<>();
for(Table table:database.getTables()) names.add(table.getName());
tableResults=createTables(dbUser,dbPassword,names,database.getCreateStatements());
}
}
File[] files=tempararyDirectory.listFiles();
if(files==null) files=new File[0];
List<Map<String,String>> generatedFiles=new ArrayList<>();
for(File f:files)
{
StringBuffer codeBuffer=new StringBuffer();
BufferedReader reader=new BufferedReader(new FileReader(f));
String line;
while((line=reader.readLine())!=null)
{
codeBuffer.append(line).append("\n");
}
reader.close();
Map<String,String> entry=new HashMap<String,String>();
entry.put("className",f.getName());
entry.put("code",codeBuffer.toString());
generatedFiles.add(entry);
}
com.google.gson.Gson gson=new com.google.gson.Gson();
response.setContentType("application/json");
Map<String,Object> result=new HashMap<>();
result.put("classes",generatedFiles);
result.put("tables",tableResults);
String json=gson.toJson(result);
response.getWriter().write(json);
}
}catch(Exception exception)
{
exception.printStackTrace();
}
}
private List<String> createTables(String username,String password,List<String> names,List<String> statements)
{
List<String> results=new ArrayList<>();
try(Connection connection=DatabaseConnector.connect(username,password))
{
for(int i=0;i<statements.size();++i)
{
String name=names.get(i);
try(java.sql.Statement statement=connection.createStatement())
{
statement.execute(statements.get(i));
results.add("Table "+name+" created");
}catch(SQLException sqlException)
{
sqlException.printStackTrace();
int code=sqlException.getErrorCode();
if(code==1050)  results.add("Table "+name+" already exists, skipped.");
else if(code==1142) results.add("Table "+name+": your MySQL user has no permission to create tables.");
else if(code==1215 || code==1824) results.add("Table "+name+": a table it refers to is missing. Create that table first.");
else results.add("Table "+name+" could not be created.");
}
}
}catch(Exception exception)
{
exception.printStackTrace();
results.add("Could not connect to your database. try after some time.");
}
return results;
}
}