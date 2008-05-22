<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<stripes:useActionBean var="bean" beanclass="sample.web.actionbeans.ReservationSuccessfulActionBean"/>
<html>
  <head><title>Reservation successful</title></head>
  <body>
    <h1>Reservation successful</h1>

	<p>Successfully reserved ${bean.task.room.description} </p>
	<p>Will bill:</p>
	<pre>
${bean.task.billingDetails}
	</pre>

  </body>
</html>

