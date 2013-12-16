<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Compare Text</title>
    </head>
    <body>
        <center>
            <form action="SubmitMessage.jsp" method="post">
                <table>
                    <tr>
                        <td>First Message :</td>
                        <td><textarea cols="30" rows="5" name="first"></textarea></td>
                    </tr>
                     <tr>
                        <td>Second Message :</td>
                        <td><textarea cols="30" rows="5" name="second"></textarea></td>
                    </tr>
                    <tr></tr>
                     <tr>
                         <td></td>
                        <td><input type="submit" value="Compare">
                    </tr>
                </table>
            </form>          
        </center>
    </body>
</html>
