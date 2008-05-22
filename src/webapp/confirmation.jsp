<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<stripes:useActionBean var="bean" beanclass="sample.web.actionbeans.ReservationSuccessfulActionBean"/>
<html>
  <head><title>Confirm purchase</title></head>
  <body>
    <h1>Confirm purchase</h1>

	<p>Please confirm reservation of ${bean.task.room.description} at the ${bean.task.hotel.description} </p>
	<p>Will bill:</p>
	<pre>
${bean.task.billingDetails}
	</pre>

    <stripes:form action="/actionbeans/ReservationSuccessful.action" focus="">
    	<stripes:hidden name="taskId" value="${task.id}"/>
    	<stripes:submit name="confirm" value="Confirm"/>                    
    </stripes:form>
  </body>
</html>

