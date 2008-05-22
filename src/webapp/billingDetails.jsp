<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<stripes:useActionBean var="bean" beanclass="sample.web.actionbeans.GetBillingInfoActionBean"/>
<html>
  <head><title>Enter billing information</title></head>
  <body>
    <h1>Enter billing information</h1>

    <stripes:form action="/actionbeans/GetBillingInfo.action" focus="">
    	<stripes:hidden name="taskId" value="${task.id}"/>
    	<stripes:textarea name="billingInfo">
    	</stripes:textarea>
    	
    	<stripes:submit name="submit" value="submit"/>        
    </stripes:form>
  </body>
</html>

