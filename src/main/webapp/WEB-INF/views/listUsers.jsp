<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib prefix="tg" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Spring 5 MVC Hibernate</title>
    </head>
    <body>
        <h2>Users List</h2>

        <div class="container" style="margin-top: 20px;">
            <jsp:useBean id="pagedListHolder" scope="request"
                         type="org.springframework.beans.support.PagedListHolder" />
            <c:url value="/listUsers" var="pagedLink">
                <c:param name="p" value="~" />
            </c:url>

            <tg:paging pagedListHolder="${pagedListHolder}"
                       pagedLink="${pagedLink}" />
            <table class="table table-bordered">
                <tr>
                    <th width="20px">Id</th>
                    <th>Name</th>
                    <th>Email</th>
                </tr>
                <c:forEach items="${pagedListHolder.pageList}" var="item">
                    <tr>
                        <td>${item.id}</td>
                        <td>${item.name}</td>
                        <td>${item.email}</td>
                    </tr>
                </c:forEach>
            </table>
            <tg:paging pagedListHolder="${pagedListHolder}"
                       pagedLink="${pagedLink}" />
        </div>



        <!--
        <table>
            <tr>
                <td><strong>Name</strong></td>
                <td><strong>Email</strong></td>
            </tr>
        <c:forEach items="${users}" var="user">
            <tr>
                <td>${user.name}</td>
                <td>${user.email}</td>
            </tr>
        </c:forEach>
    </table>
        -->
    </body>
</html>