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
        <h2>Product List</h2>
        <div class="container" style="margin-top: 20px;">
            <table>
                <tr>
                    <form:form action="updateEntry" method="post" modelAttribute="displayCriteria">
                        <td>Show Entries</td>
                        <td></td>
                        <td>
                            <form:select path="entry" onchange="this.form.submit()">
                                <form:options items="${entryList}" />
                            </form:select>
                        </td>
                    </form:form>
                    <td></td>
                    <td></td>
                    <td></td>
                    <form:form action="updateEntry" method="post" modelAttribute="displayCriteria">
                        <td>Search</td>
                        <td></td>
                        <td>
                            <form:input path="search" onchange="this.form.submit()"/>
                        </td>
                    </form:form>
                </tr>
            </table>
        </div>

        <div class="container" style="margin-top: 20px;">
            <jsp:useBean id="pagedListHolder" scope="request"
                         type="org.springframework.beans.support.PagedListHolder" />
            <c:url value="/listProducts" var="pagedLink">
                <c:param name="p" value="~" />
            </c:url>

            <tg:paging pagedListHolder="${pagedListHolder}"
                       pagedLink="${pagedLink}" />
            <table class="table table-bordered">
                <tr>
                    <th width="20px">Id</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Status</th>
                    <th>Description</th>
                </tr>
                <c:forEach items="${pagedListHolder.pageList}" var="item">
                    <tr>
                        <td>${item.id}</td>
                        <td>${item.name}</td>
                        <td>${item.price}</td>
                        <td>${item.quantity}</td>
                        <td>${item.status }</td>
                        <td>${item.description }</td>
                    </tr>
                </c:forEach>
            </table>
            <tg:paging pagedListHolder="${pagedListHolder}"
                       pagedLink="${pagedLink}" />
        </div>

    </body>
</html>