<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Countries</title>
    <style>
        .marked { color: chocolate }
    </style>
</head>
<body>
<table>

    <thead>
        <tr>
            <th>Country</th>
            <th>Population</th>
        </tr>
    </thead>

    <tbody>

        <c:forEach items="${countries}" var="count">

            <c:if test="${count.population > 100000000}">
                <tr class="marked">
                    <td>
                        <c:out value="${count.name}"/>
                    </td>
                    <td>
                        <fmt:formatNumber type="number" value="${count.population}" />
                    </td>
                </tr>
            </c:if>
            <c:if test="${count.population < 100000000}">
                <tr>
                    <td>
                        <c:out value="${count.name}"/>
                    </td>
                    <td>
                        <fmt:formatNumber type="number" value="${count.population}" />
                    </td>
                </tr>
            </c:if>
        </c:forEach>

    </tbody>
    </table>
</body>
</html>
