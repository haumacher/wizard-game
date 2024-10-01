<!DOCTYPE html>
<%@page import="de.haumacher.wizard.server.servlet.DeleteAccountConfirm"%>
<%@page import="de.haumacher.wizard.server.servlet.DeleteAccountRequest"%>
<%@page import="ch.qos.logback.core.Context"%>
<html>
<head>
	<title>Zauberer account removal</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/wizard.css">
</head>

<body>
<div class="logo">
	<img width="96" height="96" src="<%= request.getContextPath() %>/images/zauberer.png" />
</div>

<h1>Zauberer account removal</h1>

<form action="<%= request.getContextPath() %><%= DeleteAccountRequest.PATH %>" method="post">
<p>If you no longer want to play Zauberer, you can request an account removal here.</p>

<p><input type="text" name="<%=DeleteAccountConfirm.EMAIL_PARAM %>" placeholder="Your e-mail"></p>

<p><button type="submit">Delete my account!</button></p>
</form>

</body>
</html>