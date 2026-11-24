package com.customtags;
import java.util.*;
import jakarta.servlet.jsp.tagext.*;
import jakarta.servlet.jsp.*;
public class SessionContainTagSupport extends TagSupport
{
private String key;
public SessionContainTagSupport()
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
if(pageContext.getAttribute(key,PageContext.SESSION_SCOPE)!=null)
{
return EVAL_BODY_INCLUDE;
}
else
{
return SKIP_BODY;
}
}catch(Exception exception)
{
System.out.println(exception.getMessage());
}
return SKIP_BODY;
}
public int doEndTag()
{
return EVAL_PAGE;
}
}