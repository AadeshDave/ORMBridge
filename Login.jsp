<!DOCTYPE HTML>
<html lang='en'>
<head>
<title>Login Page</title>
<style>
body{
margin:0;
font-family:Arial,sans-serif;
background-color:#7aa7e6;
}
.page{
margin:20px;
padding:30px;
border:3px solid #1a3c6e;
border-radius:20px;
box-sizing:border-box;
min-height:calc(100vh - 40px);
display:flex;
flex-direction:column;
align-items:center;
justify-content:center;
}
h2{
color:#1a3c6e;
text-align:center;
}
.login{
width:340px;
padding:30px;
border:2px solid #1a3c6e;
border-radius:16px;
background:rgba(255,255,255,0.35);
}
.label{
color:#1a3c6e;
font-weight:bold;
}
.login input{
width:100%;
padding:10px;
border:2px solid #1a3c6e;
border-radius:6px;
box-sizing:border-box;
background:white;
}
.login button{
width:100%;
padding:10px;
background:#1a3c6e;
color:white;
border:none;
border-radius:6px;
cursor:pointer;
}
.login button:hover{
background:#2c5aa0;
}
.login form{
display:flex;
flex-direction:column;
gap:15px;
}
#errorMessage{
color:#b00020;
font-weight:bold;
}
</style>
<script>
window.onload=function(){
document.getElementById("loginForm").addEventListener("submit",function(event){
event.preventDefault();
var errorElement=document.getElementById("errorMessage");
var username=document.getElementById("username").value.trim();
var password=document.getElementById("password").value;
if(username.length===0 || password.length===0)
{
errorElement.innerText="Enter username and password";
return;
}
errorElement.innerText="";
var button=document.getElementById("loginButton");
button.disabled=true;
button.innerText="Connecting...";
var xmlHttpRequest=new XMLHttpRequest();
xmlHttpRequest.open("POST","login",true);
xmlHttpRequest.setRequestHeader("Content-type","application/x-www-form-urlencoded");
xmlHttpRequest.onreadystatechange=function(){
if(xmlHttpRequest.readyState!==4) return;
button.disabled=false;
button.innerText="Login";
if(xmlHttpRequest.status===200)
{
window.location="sql-to-java-page.jsp";
}
else
{
errorElement.innerText=xmlHttpRequest.responseText || "Could not connect. Try again after some time.";
document.getElementById("password").value="";
}
};
xmlHttpRequest.send("username="+encodeURIComponent(username)+"&password="+encodeURIComponent(password));
});
};
</script>
</head>
<body>
<div class='page'>
<img src='images/logo.svg' width=300 alt='ORMBridge'>
<h2>Login to MySQL</h2>
<div class='login'>
<form action='login' method='post' id='loginForm'>
<label class='label' for='username'>Username</label>
<input type='text' name='username' id='username'>
<label class='label' for='password'>Password</label>
<input type='password' name='password' id='password'>
<div id='errorMessage'></div>
<button type='submit' id='loginButton'>Login</button>
</form>
</div>
</div>
</body>
</html>