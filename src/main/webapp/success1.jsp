<%--
  Created by IntelliJ IDEA.
  User: xcy
  Date: 2019-02-14
  Time: 9:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1.0">
    <title>Insert title here</title>
</head>
<body>
<div>登陆成功！</div>
<div>用户昵称:${info.nickname}</div>
<div>用户头像:<img style="text-align: top;" width="100" src="${info.headimgurl }"></div>
</body>
</html>
