<!DOCTYPE html>
<%@page import="de.haumacher.wizard.server.servlet.AccountServlet"%>
<%@page import="de.haumacher.wizard.server.servlet.DeleteAccountConfirm"%>
<%@page import="ch.qos.logback.core.Context"%>
<html>
<head>
	<title>Confirm Zauberer account deletion</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/wizard.css">
</head>

<body>
<div class="logo">
	<img width="96" height="96" src="<%= request.getContextPath() %>/images/zauberer.png" />
</div>

<h1>Confirm Zauberer account deletion</h1>

<form action="<%= request.getContextPath() %><%= DeleteAccountConfirm.PATH %>" method="post">
<p>To confirm the deletion of your account, please enter the code sent to your e-mail address below:</p>

<p><label>E-mail:</label> <%= request.getAttribute(DeleteAccountConfirm.EMAIL_PARAM) %></p>
<p><input type="text" name="<%= AccountServlet.TOKEN_PARAM %>" placeholder="Confirmation code"></p>
<p><input type="hidden" name="<%= AccountServlet.UID_PARAM %>" value="<%= request.getAttribute(AccountServlet.UID_PARAM)%>"></p>
<p><input type="hidden" name="<%= DeleteAccountConfirm.EMAIL_PARAM %>" value="<%= request.getAttribute(DeleteAccountConfirm.EMAIL_PARAM) %>"></p>

<p><button type="submit">Confirm deletion</button></p>
</form>

</body>
</html>