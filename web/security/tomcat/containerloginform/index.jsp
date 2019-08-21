<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Home page</title>
</head>
<body>
<p>
    This is home page.
</p>

<%
    if (request.getUserPrincipal() != null) {
        out.print(request.getUserPrincipal().getName());
    }
%>


</body>
</html>
