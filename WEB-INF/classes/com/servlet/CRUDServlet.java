package com.servlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*; 
import java.util.*;
import java.sql.*;
import java.io.*;
import com.orm.db.*;
import com.crud.*;
public class CRUDServlet extends HttpServlet
{
public void doPost(HttpServletRequest request,HttpServletResponse response)
{
try
{
String action=request.getParameter("action");
action=(action==null)?"":action.trim();
HttpSession session=request.getSession(false);
if(session==null || session.getAttribute("dbUser")==null)
{
sendText(response,401,"Log in again.");
return;
}
String dbUser=(String)session.getAttribute("dbUser");
String dbPassword=(String)session.getAttribute("dbPassword");
try(Connection connection=DatabaseConnector.connect(dbUser,dbPassword))
{
if(action.equals("user"))
{
sendJSON(response,dbUser);
}
else if(action.equals("logout"))
{
session.invalidate();
sendJSON(response,"OK");
}
else if(action.equals("tables"))
{
List<String> tables=ORMOperations.dbTables(connection);
if(tables==null) 
{
sendText(response,500,"Could not load the tables. try after some time.");
}
else
{
sendJSON(response,tables);
}
}
else if(action.equals("rows") || action.equals("insert") || action.equals("update") || action.equals("delete"))
{
List<String> tables=ORMOperations.dbTables(connection);
if(tables==null)
{
sendText(response,500,"Could not load the tables. try after some time.");
return;
}
String table=request.getParameter("table");
table=(table==null)?"":table.trim();
if(!tables.contains(table))
{
sendText(response,400,"Unknown table.");
return;
}
if(action.equals("rows"))
{
Map<String,Object> rowData=ORMOperations.dbRows(connection,table);
if(rowData==null)
{
sendText(response,500,"Could not load the rows. Try again after some time.");
}
else
{
sendJSON(response,rowData);
}
}
else
{
String json=request.getParameter("data");
if(json==null || json.trim().isEmpty())
{
sendText(response,400,"No data received.");
return;
}
Map<String,String> data=new com.google.gson.Gson().fromJson(json,new com.google.gson.reflect.TypeToken<Map<String,String>>(){}.getType());
if(data==null || data.isEmpty())
{
sendText(response,400,"No data received.");
return;
}
ORMMapping mapping=new ORMMapping(connection,table,data);
if(!action.equals("insert") && (mapping.keyCount==0 || mapping.keyColumnNames.size()!=mapping.keyCount))
{
sendText(response,400,"All key columns of this table are required for this action.");
return;
}
int count;
String message;
if(action.equals("insert"))
{
count=ORMOperations.dbInsert(connection,mapping);
message="Row added";
}
else if(action.equals("update"))
{
count=ORMOperations.dbUpdate(connection,mapping);
message="Row updated";
}
else
{
count=ORMOperations.dbDelete(connection,mapping);
message="Row deleted";
}
if(count==-1)
{
sendText(response,400,"Could not save this row. Check the values and try again.");
}
else if(count==0)
{
sendText(response,400,"No row was changed.");
}
else
{
sendJSON(response,message);
}
}
}
else
{
sendText(response,400,"Invalid request");
}
}catch(SQLException sqlException)
{
sqlException.printStackTrace();
sendText(response,500,"Could not connect. Try again after some time.");
}
}catch(Exception exception)
{
exception.printStackTrace();
response.setStatus(500);}
}
public void sendText(HttpServletResponse response,int status,String text) throws IOException
{
response.setStatus(status);
response.setContentType("text/plain");
response.getWriter().write(text);
}
public void sendJSON(HttpServletResponse response,Object object) throws IOException
{
response.setContentType("application/json");
response.getWriter().write(new com.google.gson.Gson().toJson(object));
}
}