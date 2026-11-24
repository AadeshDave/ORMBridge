<%@ taglib uri="http://tomcat.apache.org/example-taglib" prefix="mytags" %>
<!DOCTYPE HTML>
<html lang='en'>
<head>
<title>SQL To java converter</title>
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
.leftPanel{
width:400px;
padding:20px;
display:flex;
flex-direction:column;
gap:15px;
}
#inputSection{
border:2px solid #1a3c6e;
border-radius:16px;
background:rgba(255,255,255,0.35);
padding:16px;
}
#fileSection{
border: 2px solid #1a3c6e;
border-radius: 16px;
background:rgba(255,255,255,0.35);
padding : 10px;
height :180px;
overflow-y:auto;
}
#fileList{
list-style:none;
margin:0;
padding:0;
}
#fileList li{
padding :6px;
border-radius: 4px;
cursor: pointer;
}
#fileList li:hover{
background:rgba(255,255,255,0.6);
}
#fileList li.active{
background: #1a3c6e;
color:white;
font-weight:bold;
}
#errorMessage{
white-space: pre-line;
}
#pageTitle{
text-align: center;
color:#1a3c6e;
}

#panelsController{
display:flex;
align-items: flex-start;
gap:30px;
}

#rightPanel{
width: 500px;
padding: 20px;
}
#sqlscript{
width: 100%;
height:200px;
padding: 10px;
font-family: monospace;
box-sizing: border-box;
}

#packageName{
width: 100%;
padding:8px;
margin-top: 10px;
box-sizing:border-box;
}
#codeToDisplay{
border: 1px solid #1a3c6e;
padding: 15px;
background: #0f2442;
color: #e6f0ff;
font-family: monospace;
height: 500px;
white-space: pre-wrap;
overflow-x:auto;
overflow-y:auto;
}
button{
padding: 8px 16px;
margin-top: 10px;
margin-right: 5px;
cursor: pointer;
}
#downloadForm{
margin-top:20px;
}
.leftPanel h2,#rightPanel h2{
color:#1a3c6e;
min-height: 30px;
margin: 0 0 10px 0;
}
.generateButton{
display:flex;
justify-content: center;
align-items : center;
}

#sqlscript,#codeToDisplay,#packageName{
border:2px solid #1a3c6e;
border-radius:6px;
}

button {
border-radius: 6px;
border:none;
background: #1a3c6e;
color:white;
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
.userBox{
background:rgba(255,255,255,0.35);
border:2px solid #1a3c6e;
border-radius:12px;
padding:15px;
color:#1a3c6e;
margin-top:15px;
}
#crudButton{
border-radius: 6px;
border:none;
background: #1a3c6e;
color:white;
}
#tableResults{
white-space:pre-line;
margin-top:8px;
font-size:14px;
}
</style>
<script>
var generatedClass=[];
var index=0;
var typing=false;
var timerId=null;
window.onload=initPage;
function initPage()
{
document.getElementById("generateButton").addEventListener("click",generateClasses);
document.getElementById("previous").addEventListener("click",function(){
pagination(this.dataset.direction);
});
document.getElementById("next").addEventListener("click",function(){
pagination(this.dataset.direction);
});
document.getElementById("downloadForm").addEventListener("submit",function(){
document.getElementById("hiddenSqlScript").value=document.getElementById("sqlscript").value;
document.getElementById("hiddenPackageName").value=document.getElementById("packageName").value;
document.getElementById("hiddenValidate").value=document.getElementById("validateWithMySQL").checked;
});
var crudButton=document.getElementById("crudButton");
if(crudButton!=null)
{
crudButton.addEventListener("click",function(){
window.location="CRUD.jsp";
});
}
}
function AJAX()
{
this.postJSON=function(urlPath,parameter,onResponse,onError){
var xmlHttpRequest=new XMLHttpRequest();
xmlHttpRequest.open("POST",urlPath,true);
xmlHttpRequest.setRequestHeader("Content-type","application/x-www-form-urlencoded");
xmlHttpRequest.onreadystatechange=function(){
if(xmlHttpRequest.readyState==4 && xmlHttpRequest.status==200)
{
onResponse(JSON.parse(xmlHttpRequest.responseText));
}
else if(xmlHttpRequest.readyState==4 && xmlHttpRequest.status==404)
{
onError("Invalid url("+urlPath+")");
}
else if(xmlHttpRequest.readyState==4 && xmlHttpRequest.status==500 || xmlHttpRequest.status==401)
{
var message=xmlHttpRequest.responseText;
onError(message && message.trim().length>0 ? message : "Invalid server error");
}
};
xmlHttpRequest.send(parameter);
};
}
function displayCurrentClass()
{
var  currentClass=generatedClass[index];
document.getElementById("currentClass").innerText=currentClass.className;
renderFileList();
setButton(false);
typeCode(currentClass.code,document.getElementById("codeToDisplay"),function(){
setButton(true);
});
}
function setButton(enabled)
{
document.getElementById("previous").disabled=!enabled || index===0;
document.getElementById("next").disabled=!enabled || index===generatedClass.length-1;
document.getElementById("downloadButton").disabled=!enabled;
}
function generateClasses()
{
if(!validation()) return;
var createBox=document.getElementById("createTables");
var tableBox=document.getElementById("tableResults");
if(tableBox!=null) tableBox.innerText="";
var createTable=(createBox!=null && createBox.checked);
if(createTable && !confirm("This will create tables in your database. Continue?"))
{
return;
}
var sqlScript=document.getElementById("sqlscript").value;
var packageName=document.getElementById("packageName").value;
var validate=document.getElementById("validateWithMySQL").checked;
var parameter="sqlScript="+encodeURIComponent(sqlScript)+"&packageName="+encodeURIComponent(packageName)+"&validateWithMySQL="+validate+(createTable?"&createTables=true":"");
var ajax=new AJAX();
ajax.postJSON("sqlToJava",parameter,function(result){
generatedClass=result.classes;
index=0;
displayCurrentClass();
if(tableBox!=null) tableBox.innerText=result.tables.join("\n");
},function(errorMessage){
document.getElementById("errorMessage").innerText=errorMessage;
});
}
function pagination(direction)
{
if(typing) return;
if(direction==="next" && index+1<generatedClass.length)
{
index++;
}
if(direction==="previous" && index>0)
{
index--;
}
displayCurrentClass();
}

function validation()
{
var sqlScript=document.getElementById("sqlscript").value.trim();
var packageName=document.getElementById("packageName").value.trim();
var errorElement=document.getElementById("errorMessage");
if(sqlScript.length===0)
{
errorElement.innerText="Enter a SQL script";
return false;
}
if(packageName.length===0)
{
errorElement.innerText="Enter package name";
return false;
}
errorElement.innerText="";
return true;
}
function renderFileList()
{
var list=document.getElementById("fileList");
list.innerHTML="";
for(var i=0;i<generatedClass.length;i++)
{
var li=document.createElement("li");
li.innerText=generatedClass[i].className;
li.dataset.index=i;
if(i===index) li.className="active";
li.addEventListener("click",function(){
if(typing) return;
index=parseInt(this.dataset.index);
displayCurrentClass();
});
list.appendChild(li);
}
}
function typeCode(code,element,onDone)
{
if(timerId) clearInterval(timerId);
element.textContent="";
var pieces=code.split(/(\s+)/);
var pos=0;
var lines=Math.max(1,Math.ceil(pieces.length/400));
typing=true;
timerId=setInterval(function(){
var end=Math.min(pos+lines,pieces.length);
element.textContent+=pieces.slice(pos,end).join("");
pos=end;
element.scrollTop=element.scrollHeight;
if(pos>=pieces.length)
{
clearInterval(timerId);
timerId=null;
typing=false;
onDone();
}
},20);
}
</script>

</head>
<body>
<div class='page'>
<h1 id='pageTitle'>SQL to java DTO (Data transfer object) converter</h1>
<div id='panelsController'>
<div class='leftPanel'>
<div id='inputSection'>
<h2> Enter your sql script here </h2>
<textarea id='sqlscript' name='sqlscript' rows='60' cols='50'></textarea>
<input type='text' id='packageName' placeholder='Package name (e.g. com.orm.dto)'>
<label><input type='checkbox' id='validateWithMySQL'>Validate with mysql</label>
<div id='errorMessage'></div>
<button type='button' id='generateButton' class='generateClasses'>Generate</button>
<mytags:SessionContain key="dbUser">
<div class='userBox'>
<input type='checkbox' id='createTables'>
<label for='createTables'>Also create these tables in my database</label>
<div id='tableResults'></div>
<button type='button' id='crudButton'>Perform crud</button>
</div>
</mytags:SessionContain>
</div>
<div id='fileSection'>
<h2> Generated Files </h2>
<ul id='fileList'></ul>
</div>
</div>
<div id='rightPanel'>
<h2 id='currentClass'></h2>
<pre id='codeToDisplay'></pre>
<button type='button' id='previous' data-direction='previous' disabled>Previous</button>
<button type='button' id='next' data-direction='next' disabled>next</button>
<form id='downloadForm' action='sqlToJava' method='post'>
<input type='hidden' name='action' value='download'>
<input type='hidden' name='sqlScript' id='hiddenSqlScript'>
<input type='hidden' name='packageName' id='hiddenPackageName'>
<input type='hidden' name='validateWithMySQL' id='hiddenValidate'>
<button type="submit" id='downloadButton' disabled>Download</button>
</form>
</div>
</div>
</div>
</body>
</html>