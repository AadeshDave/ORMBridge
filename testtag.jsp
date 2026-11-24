<%@ taglib uri="http://tomcat.apache.org/example-taglib" prefix="tags" %>
<!DOCTYPE HTML>
<html lang='en'>
<head>
<title>Tag test</title>
</head>
<body>
<h2>Tag test</h2>
<p>This line always shows.</p>
<tags:SessionContain key="dbUser">
<p style='color:green'>SessionContain: you ARE logged in (dbUser is in the session).</p>
</tags:SessionContain>
<p>This line also always shows, so SessionContain does not stop the page.</p>
<tags:SessionDoesNotContain key="dbUser">
<p style='color:red'>SessionDoesNotContain: you are NOT logged in. The rest of the page is skipped.</p>
</tags:SessionDoesNotContain>
<p>This last line shows only if you are logged in.</p>
</body>
</html>