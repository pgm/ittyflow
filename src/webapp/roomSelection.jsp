<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<stripes:useActionBean var="bean" beanclass="sample.web.actionbeans.RoomSelectionActionBean"/>
<html>
  <head><title>Choose a room</title></head>
  <body>
    <h1>Choose a room</h1>

    <stripes:form action="/actionbeans/RoomSelection.action" focus="">
    	<stripes:hidden name="taskId" value="${task.id}"/>
    	<stripes:select name="roomId">
	    	<stripes:options-collection collection="${bean.rooms}" value="id" label="description"/>
    	</stripes:select>
    	
    	<stripes:submit name="reserve" value="reserve"/>        
    </stripes:form>
  </body>
</html>

