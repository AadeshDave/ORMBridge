<!DOCTYPE html>
<html lang='en'>
<head>
<title>Session ended</title>
<style>
body{
margin:0;
font-family : Arial,sans-serif;
background-color:#7aa7e6;
display:flex;
align-items:center;
justify-content:center;
flex-direction:column;
height:100vh;
}
.message{
text-align:center;
color:#1a3c6e;
font-size:24px;
font-weight:bold;
border:3px solid #1a3c6e;
border-radius:20px;
padding:40px;
background:rgba(255,255,255,0.35);
}
button {
border-radius: 6px;
border:none;
background: #1a3c6e;
color:white;
cursor:pointer;
padding:8px 24px;
}
button:hover{
background: #2c5aa0;
}
</style>
</head>
<body>
<div class='message'>
<p>Your session ended ! Log in again</p>
</div>
<br/>
<button type='button' id='loginButton' onclick='window.location="Login.jsp"'>Login</button>
</body>
</html>