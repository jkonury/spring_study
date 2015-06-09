<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!doctype html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>home</title>
</head>
<body>
    <form:checkboxes path="interests" items="${interests}" /> <br/>
    <form:radiobuttons path="type" items="${types}" itemValue="id" itemLabel="name" /> <br/>
    
    <form:label path="type1">Type : </form:label>
    <form:select path="type1">
        <form:option value="" label="--"/>
        <form:options items="${types}" itemLabel="name" itemValue="id"/>
    </form:select>
</body>
</html>