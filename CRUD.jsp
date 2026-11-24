<%@ taglib uri="http://tomcat.apache.org/example-taglib" prefix="mytags" %>
<mytags:SessionDoesNotContain key='dbUser'>
<jsp:forward page='Login.jsp'/>
</mytags:SessionDoesNotContain>
<!DOCTYPE HTML>
<html lang='en'>
<head>
<title>C.R.U.D Operations</title>
<style>
body{
margin:0;
font-family: Arial, sans-serif;
background-color:#7aa7e6;
}
.page{
margin:20px;
padding:30px;
border:3px solid #1a3c6e;
border-radius:20px;
box-sizing:border-box;
min-height:calc(100vh - 40px);
}
#header{
display:flex;
justify-content:space-between;
align-items:center;
margin-bottom:20px;
}
#userName{
color:#1a3c6e;
font-weight:bold;
margin-right:15px;
}
#logout{
color:#1a3c6e;
font-weight:bold;
text-decoration:none;
}
#gridWrapper{
overflow-x:auto;
}
#gridArea table{
width:100%;
border-collapse:collapse;
}
#gridArea th,#gridArea td{
border:1px solid #1a3c6e;
padding:6px 10px; text-align:left;
}
#gridArea th{
background:#1a3c6e;
color:white;
}
button{
border-radius: 6px;
border:none;
background: #1a3c6e;
color:white;
padding:8px 16px;
margin-top:10px;
margin-right:5px;
cursor:pointer;
}
button:disabled{
opacity:0.5;
cursor: not-allowed;
}
button:hover{
background: #2c5aa0;
}
#errorMessage{
color: #b00020;
margin-top:5px;
font-weight:bold;
font-size:14px;
}
#panelsController{
display:flex;
align-items: flex-start;
gap:30px;
}



#tablePanel{
border: 2px solid #1a3c6e;
border-radius: 16px;
background:rgba(255,255,255,0.35);
padding : 10px;
overflow-y:auto;
width:220px;

}
#tableList{
list-style:none;
margin:0;
padding:0;
}
#tableList li{
padding :6px;
border-radius: 4px;
cursor: pointer;
}
#tableList li:hover{
background:rgba(255,255,255,0.6);
}
#tableList li.active{
background: #1a3c6e;
color:white;
font-weight:bold;
}
#mainPanel{
border:2px solid #1a3c6e;
border-radius:16px;
background:rgba(255,255,255,0.35);
padding:16px;
flex:1;
min-width:0;
}
#tablePanel h2,#mainPanel h2{
color:#1a3c6e;
min-height: 30px;
margin: 0 0 10px 0;
}
#pageTitle{
text-align: center;
color:#1a3c6e;
}
#infoMessage{
color:#1a3c6e;
font-weight:bold;
margin-right:5px;
}
#gridNote{
color:#1a3c6e;
font-size:13px;
margin-bottom:6px;
}
#gridArea td button{
margin-top:0;
padding:4px 10px;
}
#formArea:empty{
display:none;
}
#formArea{
margin-top:15px;
padding:12px;
border:2px solid #1a3c6e;
border-radius:12px;
background:rgba(255,255,255,0.6);
line-height:2;
}
#formArea input{
padding:5px;
border:1px solid #1a3c6e;
border-radius:6px;
}
#formArea input:read-only{
background:#e6e6e6;
}
</style>
<script>
var currentTable;
var currentData;
function AJAX()
{
this.postJSON=function(urlPath,parameter,onResponse,onError){
var xmlHttpRequest=new XMLHttpRequest();
xmlHttpRequest.open("POST",urlPath,true);
xmlHttpRequest.setRequestHeader("Content-type","application/x-www-form-urlencoded");
xmlHttpRequest.onreadystatechange=function(){
if(xmlHttpRequest.readyState!=4) return;
if(xmlHttpRequest.status==200)
{
onResponse(JSON.parse(xmlHttpRequest.responseText));
}
else if(xmlHttpRequest.status==404)
{
onError("Invalid url("+urlPath+")");
}
else
{
var message=xmlHttpRequest.responseText;
onError(message && message.trim().length>0 ? message : "Invalid server error");
}
};
xmlHttpRequest.send(parameter);
};
}
window.onload=initPage;
function initPage()
{
var ajax=new AJAX();
ajax.postJSON("crud","action=user",function(result){
document.getElementById("userName").textContent=result;
},showError);
ajax.postJSON("crud","action=tables",function(result){
showTables(result);
},showError);
document.getElementById("gridArea").onclick=function(event){
var id=event.target.id;
if(id.indexOf("edit")==0) showForm(id.substring(4));
if(id.indexOf("delete")==0) deleteRow(id.substring(6));
};
document.getElementById("logout").addEventListener("click",function(event){
event.preventDefault();
new AJAX().postJSON("crud","action=logout",function(){
window.location="SessionEnded.jsp";
},showError);
});
}
function showError(message)
{
document.getElementById("errorMessage").textContent=message;
}
function showTables(names)
{
var tableList=document.getElementById("tableList");
tableList.innerHTML="";
for(var i=0;i<names.length;i++)
{
var li=document.createElement("li");
li.textContent=names[i];
li.addEventListener("click",function(){
selectTable(this);
});
tableList.appendChild(li);
}
}

function selectTable(items)
{
var allItems=document.querySelectorAll("#tableList li");
for(var i=0;i<allItems.length;i++) allItems[i].className="";
currentTable=items.textContent;
document.getElementById("formArea").innerHTML="";
items.className="active";
document.getElementById("tableTitle").textContent=currentTable;
document.getElementById("errorMessage").textContent="";
document.getElementById("infoMessage").textContent="";
new AJAX().postJSON("crud","action=rows&table="+encodeURIComponent(currentTable),drawGrid,showError);
}
function drawGrid(rowData)
{
currentData=rowData;
var html="<table><tr>";
for(var i=0;i<rowData.columns.length;i++) html+="<th>"+rowData.columns[i].name+"</th>";
if(!rowData.readOnly) html+="<th></th>";
html+="</tr>";
for(var r=0;r<rowData.rows.length;r++)
{
html+="<tr>";
for(var c=0;c<rowData.rows[r].length;c++) html+="<td>"+rowData.rows[r][c]+"</td>";
if(!rowData.readOnly) html+="<td><button class='editButton' id='edit"+r+"'>Edit</button><button class='deleteButton' id='delete"+r+"'>Delete</button></td>";
html+="</tr>";
}
document.getElementById("gridArea").innerHTML=html+"</table>";
document.getElementById("addButton").disabled=rowData.readOnly;
}
function showForm(index)
{
var html="";
for(var i=0;i<currentData.columns.length;i++)
{
var column=currentData.columns[i];
if(index==-1 && column.autoIncrement) continue;
var value="";
var readonly="";
if(index!=-1)
{
value=currentData.rows[index][i];
if(value==null) value="";
if(column.primaryKey) readonly=" readonly";
}
html+=column.name+": <input id='field"+i+"' value='"+value+"'"+readonly+"><br>";
}
if(index==-1) html+="<button onclick='addRow()'>Save</button>";
else html+="<button onclick='updateRow()'>Save</button>";
html+="<button onclick=\"document.getElementById('formArea').innerHTML=''\">Cancel</button>";
document.getElementById("formArea").innerHTML=html;
}
function addRow()
{
var data={};
for(var i=0;i<currentData.columns.length;i++)
{
var column=currentData.columns[i];
if(column.autoIncrement) continue;
data[column.name]=document.getElementById("field"+i).value;
}
var parameter="action=insert&table="+encodeURIComponent(currentTable)+"&data="+encodeURIComponent(JSON.stringify(data));
new AJAX().postJSON("crud",parameter,function(message){
selectTable(document.querySelector("#tableList li.active"));
document.getElementById("infoMessage").textContent=message;
},showError);
}


function updateRow()
{
var data={};
for(var i=0;i<currentData.columns.length;i++)
{
data[currentData.columns[i].name]=document.getElementById("field"+i).value;
}
var parameter="action=update&table="+encodeURIComponent(currentTable)+"&data="+encodeURIComponent(JSON.stringify(data));
new AJAX().postJSON("crud",parameter,function(message){
selectTable(document.querySelector("#tableList li.active"));
document.getElementById("infoMessage").textContent=message;
},showError);
}

function deleteRow(index)
{
var row=currentData.rows[index];
if(!confirm("Delete this row?")) return;
var data={};
for(var i=0;i<currentData.columns.length;i++)
{
if(currentData.columns[i].primaryKey) data[currentData.columns[i].name]=row[i];
}
var parameter="action=delete&table="+encodeURIComponent(currentTable)+"&data="+encodeURIComponent(JSON.stringify(data));
new AJAX().postJSON("crud",parameter,function(message){
selectTable(document.querySelector("#tableList li.active"));
document.getElementById("infoMessage").textContent=message;
},showError);
}
</script>
</head>
<body>
<div class='page'>
<div id='header'>
<img src='images/logo.svg' width='150' alt='ORMBridge'>
<div>
<span id='userName'></span>
<a id='logout' href='#'>Log out</a>
</div>
</div>
<div id='errorMessage'></div>
<div id='infoMessage'></div>

<div id='panelsController'>
<div id='tablePanel'>
<h2>Tables</h2>
<ul id='tableList'></ul>
</div>
<div id='mainPanel'>
<h2 id='tableTitle'>Select a table</h2>
<div id='gridWrapper'>
<div id='gridNote'></div>
<div id='gridArea'>
</div>
</div>
<button type='button' id='addButton' onclick='showForm(-1)' disabled>Add row</button>
<div id='formArea'></div>
</div>
</div>
</div>
</body>
</html>