<!DOCTYPE HTML>
<html lang='en'>
<head>
<title>ORMBridge</title>
<style>
body{
background-color : #7aa7e6;
font-family: Arial, sans-serif;
margin:0;
}
.page{
margin:20px;
padding:30px;
border:3px solid #1a3c6e;
border-radius:20px;
min-height:calc(100vh - 100px);
text-align:center;
}
.links{
display : flex;
gap : 40px;
margin-top:40px;
justify-content:center;
flex-wrap:wrap;
padding:0 20px;
}
.description{
max-width:600px;
margin:20px auto 30px auto;
line-height:1.6;
font-size:16px;
color:white;
}
.names{
display:flex;
flex-direction:column;
align-items:center;
gap:15px;
width:240px;
padding:25px;
border:2px solid #1a3c6e;
border-radius:16px;
background:rgba(255,255,255,0.35);
color:#1a3c6e;
font-size:20px;
font-weight:bold;
text-decoration:none;
}
.about{
max-width:800px;
margin:50px auto 0 auto;
text-align:left;
}
.about h2{
color:#1a3c6e;
font-size:24px;
margin:30px 0 10px 0;
}
.about p,.about li{
color:white;
font-size:17px;
line-height:1.7;
margin-bottom:10px;
}
.about strong{
color:#1a3c6e;
}
</style>
</head>
<body>
<div class='page'>
<img src='images/logo.svg' width=300>
<p class='description'>ORMBridge turns your SQL CREATE TABLE scripts into ready-to-use Java classes for your ORM. Paste your script, choose a package name, and get one class per table. Each class has private fields, getters and setters, a default constructor, and equals, hashCode and compareTo methods built on the key columns.</p>
<div class='links'>
<a class='names' href='sql-to-java-page.jsp'>
<img src='images/generate.png' width=100 alt='ORMBridge'>
<span>Generate classes</span>
</a>
<a class='names' href='Login.jsp'>
<img src='images/crud.png' width=100 alt='ORMBridge'>
<span>Perform CRUD</span>
</a>
</div>
<div class='about'>
<h2>What is ORMBridge?</h2>
<p>ORMBridge reads your <strong>CREATE TABLE</strong> scripts and writes one Java class per table, with fields, getters and setters, a default constructor, and equals, hashCode and compareTo methods.</p>
<h2>What it detects</h2>
<ul>
<li><strong>Primary and composite keys:</strong> marked with annotations.</li>
<li><strong>Foreign keys:</strong> linked to the parent table and column.</li>
<li><strong>Column details:</strong> names, sizes and precision.</li>
<li><strong>Check constraints:</strong> carried into the class.</li>
<li><strong>MySQL validation (optional):</strong> your script is tested before generating.</li>
</ul>
<h2>How it works</h2>
<ol>
<li>Paste your CREATE TABLE script.</li>
<li>Enter a package name.</li>
<li>Press Generate, then download the zip.</li>
</ol>
</div>
</div>
</body>
</html>
