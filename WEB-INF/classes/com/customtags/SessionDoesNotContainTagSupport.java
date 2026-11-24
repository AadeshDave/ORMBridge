package com.customtags;
import java.util.*;
import jakarta.servlet.jsp.tagext.*;
import jakarta.servlet.jsp.*;
public class SessionDoesNotContainTagSupport extends TagSupport
{
private String key;
private boolean processPage;
public SessionDoesNotContainTagSupport()
{
this.key="user";
}
public void setKey(String key)
{
this.key=key;
}
public String getKey()
{
return this.key;
}
public int doStartTag()
{
try
{
System.out.println("doStart");
processPage=(pageContext.getAttribute(key,PageContext.SESSION_SCOPE)!=null);
if(!processPage) return EVAL_BODY_INCLUDE;
}catch(Exception exception)
{
System.out.println(exception.getMessage());
}
return SKIP_BODY;
}
public int doEndTag()
{
System.out.println("doEnd");
return (processPage)?EVAL_PAGE:SKIP_PAGE;
}
}