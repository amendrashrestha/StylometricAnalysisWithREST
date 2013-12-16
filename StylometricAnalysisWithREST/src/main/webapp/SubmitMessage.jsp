<%-- 
    Document   : SubmitMessage
    Created on : Dec 15, 2013, 1:22:30 PM
    Author     : ITE
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Submit Message Page</title>
    </head>
    <body>
        <%
        String firstMessage = request.getParameter("first");
        String secondMessage = request.getParameter("second");
        %>
    <table>
        <tbody>
            <tr>
                <td>First Message:</td>
                <td></td>
                <td><%=firstMessage%></td>
            </tr>
            <tr>
                <td>Second Message:</td>
                <td></td>
                <td><%=secondMessage%></td>
            </tr>
        </tbody>
    </table>
    </body>
</html>
