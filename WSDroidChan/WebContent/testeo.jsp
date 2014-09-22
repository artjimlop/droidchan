<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@page import="dao.*"%>
<%@page import="domain.*"%>
<%@page import="utils.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
	
	 User s = new JDBCUserDAO().getAllUsers().get(0);
		
	%>
	
	<div id="div_name_main_user"><%= s.getLogin() + " AHORA PASS: " +s.getPassword() %></div>
	
</body>
</html>