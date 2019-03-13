<%--
  Created by IntelliJ IDEA.
  User: xcy
  Date: 2019-02-14
  Time: 9:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0">
    <title>Insert title here</title>
</head>
<body>
<form action="/WxAuth/wxCallBack" method="post">
    账号:<input type="text" name="account"/><br/>
    密码:<input type="password" name="password"/><br/>
    openId: <input type="hidden" name="openid" value="${openid}"  readonly="readonly"/>
    <input type="text" value="${openid}"/><br/>
    昵称:<input type="text" name="nickname" value="${nickname}" readonly="readonly"/><br/>
    <input type="submit" value="提交并绑定"/>
</form>
</body>
</html>

