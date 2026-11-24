package com.servlet;
import jakarta.servlet.http.*;
import jakarta.servlet.*;
import java.io.*;
import java.sql.*;
import com.orm.db.*;
public class LoginServlet extends HttpServlet
{
public void doPost(HttpServletRequest request,HttpServletResponse response)
{
try
{
String username=request.getParameter("username");
String password=request.getParameter("password");
username=(username==null)?"":username.trim();
password=(password==null)?"":password;
response.setContentType("text/plain");
if(username.isEmpty() || password.isEmpty())
{
response.setStatus(400);
response.getWriter().write("Enter username and password");
return;
}
try(Connection connection=DatabaseConnector.connect(username,password);Statement statement=connection.createStatement())
{
statement.execute("Select 1");
HttpSession session=request.getSession(true);
session.setAttribute("dbUser",username);
session.setAttribute("dbPassword",password);
session.setMaxInactiveInterval(900);
response.getWriter().write("done");
}catch(SQLException sqlException)
{
sqlException.printStackTrace();
if(sqlException.getErrorCode()==1045)
{
response.setStatus(401);
response.getWriter().write("Invalid username or password");
}
else
{
response.setStatus(500);
response.getWriter().write("Could not connect ! try after some time.");
}
}catch(Exception exception)
{
exception.printStackTrace();
response.setStatus(500);
response.getWriter().write("Could not connect. Try again after some time.");
}
}catch(Exception exception)
{
exception.printStackTrace();
}
}
}