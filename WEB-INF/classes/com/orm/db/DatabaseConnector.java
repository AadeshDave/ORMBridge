package com.orm.db;
import java.sql.*;
import java.io.*;
import java.util.*;
public class DatabaseConnector 
{
private DatabaseConnector() {};
static public Connection connect() throws Exception
{
Connection connection=null;
File file=new File("C:\\apachetomcat11\\webapps\\sql-to-java\\WEB-INF\\db.conf");
if(file.exists()==false) throw new Exception("db.conf missing");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new Exception("db.conf is empty");
}
Map<String,String> keyValues=new HashMap<>();
String line;
String splits[];
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
line=randomAccessFile.readLine();
splits=line.split("=");
if(splits.length==2)
{
keyValues.put(splits[0].trim().toUpperCase(),splits[1].trim());
}
}
randomAccessFile.close();
String protocol=keyValues.get("PROTOCOL");
if(protocol==null) throw new Exception("Protocol is messing");
String username=keyValues.get("USERNAME");
if(username==null) throw new Exception("Username is messing");
String password=keyValues.get("PASSWORD");
if(password==null) throw new Exception("Password is messing");
String database=keyValues.get("DATABASE");
if(database==null) throw new Exception("Database is messing");
String driver=keyValues.get("DRIVER");
if(driver==null) throw new Exception("Driver is messing");
String server=keyValues.get("SERVER");
if(server==null) throw new Exception("Server is messing");
String port=keyValues.get("PORT");
if(port==null) throw new Exception("Port is messing");
Class.forName(driver);
connection=DriverManager.getConnection(protocol+"://"+server+":"+port+"/"+database,username,password);
return connection;
}
public static Connection connect(String username,String password) throws Exception
{
Connection connection=null;
File file=new File("C:\\apachetomcat11\\webapps\\sql-to-java\\WEB-INF\\db.conf");
if(file.exists()==false) throw new Exception("db.conf missing");
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new Exception("db.conf is empty");
}
Map<String,String> keyValues=new HashMap<>();
String line;
String splits[];
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
line=randomAccessFile.readLine();
splits=line.split("=");
if(splits.length==2)
{
keyValues.put(splits[0].trim().toUpperCase(),splits[1].trim());
}
}
randomAccessFile.close();
String protocol=keyValues.get("PROTOCOL");
if(protocol==null) throw new Exception("Protocol is messing");
String database=keyValues.get("DATABASE");
if(database==null) throw new Exception("Database is messing");
String driver=keyValues.get("DRIVER");
if(driver==null) throw new Exception("Driver is messing");
String server=keyValues.get("SERVER");
if(server==null) throw new Exception("Server is messing");
String port=keyValues.get("PORT");
if(port==null) throw new Exception("Port is messing");
Class.forName(driver);
connection=DriverManager.getConnection(protocol+"://"+server+":"+port+"/"+database,username,password);
return connection;
}
}