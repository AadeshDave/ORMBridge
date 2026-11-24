package com.generator;
import com.orm.db.*;
import com.orm.db.ForeignKey;
import com.orm.db.ForeignKeyMapping;
import java.util.*;
import java.io.*;
import com.orm.annotation.*;
public class DTOWriter
{
private static Map<String,String> javaTypes;
private static Map<String,String> defaultValues;
static
{
javaTypes=new HashMap<String,String> ();
javaTypes.put("INT","int");
javaTypes.put("BIGINT","long");
javaTypes.put("SMALLINT","short");
javaTypes.put("TINYINT","byte");
javaTypes.put("DECIMAL","java.math.BigDecimal");
javaTypes.put("FLOAT","float");
javaTypes.put("DOUBLE","double");
javaTypes.put("VARCHAR","String");
javaTypes.put("TEXT","String");
javaTypes.put("CHAR","String");
javaTypes.put("DATE","java.sql.Date");
javaTypes.put("DATETIME","java.sql.Timestamp");
javaTypes.put("TIMESTAMP","java.sql.Timestamp");
javaTypes.put("BOOLEAN","boolean");
// add more (do it on your own)
defaultValues=new HashMap<String,String>();
defaultValues.put("int","0");
defaultValues.put("long","0");
defaultValues.put("short","0");
defaultValues.put("byte","0");
defaultValues.put("java.math.BigDecimal","new java.math.BigDecimal(\"0.0\")");
defaultValues.put("float","0.0f");
defaultValues.put("double","0.0");
defaultValues.put("String","\"\"");
defaultValues.put("String","\"\"");
defaultValues.put("String","\"\"");
defaultValues.put("java.sql.Date","null");
defaultValues.put("java.sql.Timestamp","null");
defaultValues.put("java.sql.Timestamp","null");
defaultValues.put("boolean","false");
}
public DTOWriter()
{
}
public static String getName(String tableName)
{
String[] splitsClasses=tableName.split("_");
String className="";
for(String sp:splitsClasses)
{
if(sp.trim().length()==0) continue;
className+=sp.substring(0,1).toUpperCase()+sp.substring(1).toLowerCase();
}
return className;
}
public static String getPropertyName(String columnName)
{
String[] splitsProperties=columnName.split("_");
String propertyName="";
boolean first=true;
for(String sp:splitsProperties)
{
if(sp.trim().length()==0) continue;
if(first)
{
propertyName+=sp.substring(0,1).toLowerCase()+sp.substring(1).toLowerCase();
first=false;
}
else
{
propertyName+=sp.substring(0,1).toUpperCase()+sp.substring(1).toLowerCase();
}
}
return propertyName;
}
public static String getType(String sqlType)
{
sqlType=sqlType.toUpperCase();
if(sqlType.indexOf("BIGINT")!=-1) return "long";
if(sqlType.indexOf("SMALLINT")!=-1) return "short";
if(sqlType.indexOf("TINYINT")!=-1) return "byte";
if(sqlType.indexOf("INT")!=-1) return "int";
if(sqlType.indexOf("DECIMAL")!=-1 || sqlType.indexOf("NUMERIC")!=-1) return "java.math.BigDecimal";
if(sqlType.indexOf("DOUBLE")!=-1) return "double";
if(sqlType.indexOf("FLOAT")!=-1) return "float";
if(sqlType.indexOf("TEXT")!=-1 || sqlType.indexOf("CHAR")!=-1) return "String";
if(sqlType.indexOf("DATE")!=-1 || sqlType.indexOf("TIME")!=-1) return "java.sql.Timestamp";
if(sqlType.indexOf("BOOLEAN")!=-1 || sqlType.indexOf("BIT")!=-1) return "boolean";
return "String";
}
public static void generate(Database database,String packageName,File outputFile) throws  Exception
{
try
{
List<com.orm.db.Table> tables=database.getTables();
List<TableColumn> tableColumns;
Vector<String> properties=new Vector<>();
Vector<String> propertyTypes=new Vector<>();
String javaPropertyName;
String javaPropertyType;
if(tables.size()==0) throw new Exception("Nothing to generate");
String javaFileName;
File javaFile;
int index=0;
String className;
String propertyName;
String tableName;
String defaultValue;
RandomAccessFile randomAccessFile;
String line;
for(com.orm.db.Table t:tables)
{
tableName=t.getName();
className=getName(tableName);
javaFileName=className+".java";
javaFile=new File(outputFile,javaFileName);
if(javaFile.exists()) javaFile.delete();
randomAccessFile=new RandomAccessFile(javaFile,"rw");
line="package "+packageName+";\n";
randomAccessFile.writeBytes(line);
tableColumns=t.getTableColumns();
List<TableColumn> primaryKeyColumns=new ArrayList<>();
for(TableColumn tc:tableColumns)
{
if(tc.getHasPrimaryKeyConstrientApplied()) primaryKeyColumns.add(tc);
}
boolean hasSingleKey=(primaryKeyColumns.size()==1);
boolean hasCompositeKey=(primaryKeyColumns.size()>=2);
boolean hasForeignKey=(t.getForeignKey()!=null && !t.getForeignKey().isEmpty());
boolean isAutoIncrementable=false;
boolean anyColumnHasWidth=false;
boolean anyColumnHasCheck=false;
for(TableColumn tc:tableColumns)
{
if(tc.getWidth()>0) anyColumnHasWidth=true;
}
for(TableColumn tc:tableColumns)
{
if(tc.getCheckConstraint()!=null && !tc.getCheckConstraint().isEmpty()) anyColumnHasCheck=true;
}
for(TableColumn tc:tableColumns)
{
if(tc.getIsAutoIncrementable()) isAutoIncrementable=true;
}
if(isAutoIncrementable) randomAccessFile.writeBytes("import com.orm.annotation.Auto;\n");
if(hasSingleKey) randomAccessFile.writeBytes("import com.orm.annotation.PrimaryKey;\n");
if(hasCompositeKey) randomAccessFile.writeBytes("import com.orm.annotation.CompositeKey;\n");
if(hasForeignKey) randomAccessFile.writeBytes("import com.orm.annotation.ForeignKey;\n");
if(anyColumnHasWidth) randomAccessFile.writeBytes("import com.orm.annotation.MaxLength;\n");
if(anyColumnHasCheck) randomAccessFile.writeBytes("import com.orm.annotation.Check;\n");
randomAccessFile.writeBytes("import com.orm.annotation.Column;\n");
if(hasCompositeKey)
{
StringBuffer columnList=new StringBuffer();
for(int i=0;i<primaryKeyColumns.size();i++)
{
if(i>0) columnList.append(",");
columnList.append("\"").append(primaryKeyColumns.get(i).getName()).append("\"");
}
randomAccessFile.writeBytes("@CompositeKey(columns={"+columnList+"},table=\""+tableName+"\")\n");
}
line="public class "+className+" implements java.io.Serializable,Comparable<"+className+">\n";
randomAccessFile.writeBytes(line);
line="{\n";
randomAccessFile.writeBytes(line);
// property decelaration
properties.clear();
propertyTypes.clear();
for(TableColumn tableColumn:tableColumns)
{
javaPropertyName=getPropertyName(tableColumn.getName());
properties.add(javaPropertyName);
javaPropertyType=getType(tableColumn.getType());
propertyTypes.add(javaPropertyType);
if(hasSingleKey && tableColumn.getHasPrimaryKeyConstrientApplied())
{
randomAccessFile.writeBytes("@PrimaryKey\n");
}
if(tableColumn.getIsAutoIncrementable())
{
randomAccessFile.writeBytes("@Auto\n");
}
if(hasForeignKey)
{
for(ForeignKey fk:t.getForeignKey())
{
for(ForeignKeyMapping mapping:fk.getForeignKeyMapping())
{
if(mapping.getChildTableColumnName().equalsIgnoreCase(tableColumn.getName()))
{
randomAccessFile.writeBytes("@ForeignKey(table=\""+fk.getParentChildTable()+"\",column=\""+mapping.getParentTableColumnName()+"\")\n");
}
}
}
}
randomAccessFile.writeBytes("@Column(name=\""+tableColumn.getName()+"\")\n");
if(tableColumn.getWidth()>0) randomAccessFile.writeBytes("@MaxLength("+tableColumn.getWidth()+")\n");
String checkString=tableColumn.getCheckConstraint();
if(checkString!=null && !checkString.isEmpty())
{
checkString=checkString.replace("\\","\\\\").replace("\"","\\\"");
randomAccessFile.writeBytes("@Check(constraint=\""+checkString+"\")\n");
}
line="private "+javaPropertyType+" "+javaPropertyName+";\n";
randomAccessFile.writeBytes(line);
}
// default constructure starts
line="public "+className+"()\n";
randomAccessFile.writeBytes(line);
line="{\n";
randomAccessFile.writeBytes(line);
index=0;
for(String p:properties)
{
defaultValue=defaultValues.get(propertyTypes.get(index));
line="this."+p+"="+defaultValue+";\n";
randomAccessFile.writeBytes(line);
++index;
}
line="}\n";
randomAccessFile.writeBytes(line);
line="public "+className+"(";
randomAccessFile.writeBytes(line);
index=0;
line=propertyTypes.get(0)+" "+properties.get(0);
index=1;
while(index<properties.size())
{
line+=(","+propertyTypes.get(index)+" "+properties.get(index));
++index;
}
randomAccessFile.writeBytes(line);
line=")\n";
randomAccessFile.writeBytes(line);
line="{\n";
randomAccessFile.writeBytes(line);
for(String p:properties)
{
line="this."+p+"="+p+";\n";
randomAccessFile.writeBytes(line);
}
line="}\n";
randomAccessFile.writeBytes(line);
// parameterized  constructor starts (leave auto fields)
// setter starts
index=0;
String upperName;
for(String p:properties)
{
upperName=p.substring(0,1).toUpperCase()+p.substring(1);
line="public void set"+upperName+"("+propertyTypes.get(index)+" "+p+")\n";
randomAccessFile.writeBytes(line);
line="{\n";
randomAccessFile.writeBytes(line);
line="this."+p+"="+p+";\n";
randomAccessFile.writeBytes(line);
line="}\n";
randomAccessFile.writeBytes(line);
index++;
}
// getter starts
index=0;
for(String p:properties)
{
upperName=p.substring(0,1).toUpperCase()+p.substring(1);
line="public "+propertyTypes.get(index)+" get"+upperName+"("+")\n";
randomAccessFile.writeBytes(line);
line="{\n";
randomAccessFile.writeBytes(line);
line="return this."+p+";\n";
randomAccessFile.writeBytes(line);
line="}\n";
randomAccessFile.writeBytes(line);
index++;
}
// equals starts
line="public boolean"+" "+"equals(Object object)\n";
randomAccessFile.writeBytes(line);
line="{\n";
randomAccessFile.writeBytes(line);
line="if(this==object) return true;\n";
randomAccessFile.writeBytes(line);
line="if(object==null || getClass()!=object.getClass()) return false;\n";
randomAccessFile.writeBytes(line);
line=className+" other=("+className+")object;\n";
randomAccessFile.writeBytes(line);
Vector<String> comparision=new Vector<>();
Vector<String> hashProperties=new Vector<>();
if(hasSingleKey || hasCompositeKey)
{
for(TableColumn tc:primaryKeyColumns)
{
String p=getPropertyName(tc.getName());
hashProperties.add(p);
String c;
if(isPrimitive(getType(tc.getType()))) c="this."+p+"==other."+p;
else c="java.util.Objects.equals(this."+p+",other."+p+")";
comparision.add(c);
}
}
else
{
for(TableColumn tc:tableColumns)
{
String p=getPropertyName(tc.getName());
hashProperties.add(p);
String c;
if(isPrimitive(getType(tc.getType()))) c="this."+p+"==other."+p;
else c="java.util.Objects.equals(this."+p+",other."+p+")";
comparision.add(c);
}
}
String completeReturnStatement=String.join(" && ",comparision);
line="return "+completeReturnStatement+";\n";
randomAccessFile.writeBytes(line);
line="}\n";
randomAccessFile.writeBytes(line);
// compareTo starts
line="public int compareTo("+className+" object)\n";
randomAccessFile.writeBytes(line);
line="{\n";
randomAccessFile.writeBytes(line);
line=className+" other=object;\n";
randomAccessFile.writeBytes(line);
List<String> comparisonVariableNames=new ArrayList<>();
String propertyAtZeroIndex="";
String typeAtZeroIndex="";
if(hasCompositeKey)
{
for(TableColumn tc:primaryKeyColumns)
{
propertyAtZeroIndex=getPropertyName(tc.getName());
typeAtZeroIndex=getType(tc.getType());
String comparisonExpression;
if(isPrimitive(typeAtZeroIndex))
{
if(typeAtZeroIndex.equals("int")) comparisonExpression="Integer.compare(this."+propertyAtZeroIndex+",other."+propertyAtZeroIndex+")";
else if(typeAtZeroIndex.equals("long")) comparisonExpression="Long.compare(this."+propertyAtZeroIndex+",other."+propertyAtZeroIndex+")";
else if(typeAtZeroIndex.equals("short")) comparisonExpression="Short.compare(this."+propertyAtZeroIndex+",other."+propertyAtZeroIndex+")";
else if(typeAtZeroIndex.equals("byte")) comparisonExpression="Byte.compare(this."+propertyAtZeroIndex+",other."+propertyAtZeroIndex+")";
else if(typeAtZeroIndex.equals("float")) comparisonExpression="Float.compare(this."+propertyAtZeroIndex+",other."+propertyAtZeroIndex+")";
else if(typeAtZeroIndex.equals("double")) comparisonExpression="Double.compare(this."+propertyAtZeroIndex+",other."+propertyAtZeroIndex+")";
else  comparisonExpression="Boolean.compare(this."+propertyAtZeroIndex+",other."+propertyAtZeroIndex+")";
}
else
{
comparisonExpression="this."+propertyAtZeroIndex+".compareTo(other."+propertyAtZeroIndex+")";
}
String smf="c"+index;
line="int "+smf+"="+comparisonExpression+";\n";
randomAccessFile.writeBytes(line);
comparisonVariableNames.add(smf);
index++;
}
for(int i=0;i<comparisonVariableNames.size()-1;++i)
{
String nameOfVariable=comparisonVariableNames.get(i);
line="if("+nameOfVariable+"!=0) return "+nameOfVariable+";\n";
randomAccessFile.writeBytes(line);
}
line="return "+comparisonVariableNames.get(comparisonVariableNames.size()-1)+";\n";
randomAccessFile.writeBytes(line);
}
else
{
if(hasSingleKey)
{
propertyAtZeroIndex=getPropertyName(primaryKeyColumns.get(0).getName());
typeAtZeroIndex=getType(primaryKeyColumns.get(0).getType());
}
else
{
propertyAtZeroIndex=properties.get(0);
typeAtZeroIndex=propertyTypes.get(0);
}
String comparisonExpression;
if(isPrimitive(typeAtZeroIndex))
{
if(typeAtZeroIndex.equals("int")) comparisonExpression="Integer.compare(this."+propertyAtZeroIndex+",other."+propertyAtZeroIndex+")";
else if(typeAtZeroIndex.equals("long")) comparisonExpression="Long.compare(this."+propertyAtZeroIndex+",other."+propertyAtZeroIndex+")";
else if(typeAtZeroIndex.equals("short")) comparisonExpression="Short.compare(this."+propertyAtZeroIndex+",other."+propertyAtZeroIndex+")";
else if(typeAtZeroIndex.equals("byte")) comparisonExpression="Byte.compare(this."+propertyAtZeroIndex+",other."+propertyAtZeroIndex+")";
else if(typeAtZeroIndex.equals("float")) comparisonExpression="Float.compare(this."+propertyAtZeroIndex+",other."+propertyAtZeroIndex+")";
else if(typeAtZeroIndex.equals("double")) comparisonExpression="Double.compare(this."+propertyAtZeroIndex+",other."+propertyAtZeroIndex+")";
else  comparisonExpression="Boolean.compare(this."+propertyAtZeroIndex+",other."+propertyAtZeroIndex+")";
}
else
{
comparisonExpression="this."+propertyAtZeroIndex+".compareTo(other."+propertyAtZeroIndex+")";
}
line="return "+comparisonExpression+";\n";
randomAccessFile.writeBytes(line);
}
line="}\n";
randomAccessFile.writeBytes(line);
// hashCode starts
line="public int hashCode()\n";
randomAccessFile.writeBytes(line);
line="{\n";
randomAccessFile.writeBytes(line);
// code to hashCode()
String allProperties=String.join(", ",hashProperties);
line="return java.util.Objects.hash("+allProperties+");\n";
randomAccessFile.writeBytes(line);
line="}\n";
randomAccessFile.writeBytes(line);
// optional, toString strats
line="}\n";// class ends
randomAccessFile.writeBytes(line);
randomAccessFile.close();
}
}catch(Exception exception)
{
System.out.println(exception.getMessage());
}
}
public static boolean isPrimitive(String type)
{
return type.equals("int") || type.equals("long") || type.equals("short") || type.equals("byte") || type.equals("float") || type.equals("double") ||  type.equals("boolean");
}
}