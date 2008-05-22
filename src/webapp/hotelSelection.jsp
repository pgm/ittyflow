<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<stripes:useActionBean var="bean" beanclass="sample.web.actionbeans.HotelSelectionActionBean"/>
<html>
  <head><title>Search hotels</title></head>
  <body>
    <h1>Pick a hotel</h1>

    <stripes:form action="/actionbeans/HotelSelection.action" focus="">
    	<stripes:hidden name="taskId" value="${task.id}"/>
    	<stripes:select name="hotelId">
	    	<stripes:options-collection collection="${bean.hotels}" value="id" label="description"/>
    	</stripes:select>
    	
    	<stripes:submit name="selectHotel" value="Search"/>                    
    </stripes:form>
  </body>
</html>

