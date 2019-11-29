<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/11/19
  Time: 11:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>login</title>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/layui/css/layui.css">
    <script src="<%=request.getContextPath()%>/layui/layui.all.js"></script>
</head>
<div>

<form class="layui-form" action="" >
    <div class="layui-form-item">
        <label class="layui-form-label">姓名:</label>
        <div class="layui-input-block">
            <input type="text"  style="width:200px;" name="title" lay-verify="title" autocomplete="off" placeholder="请输入姓名" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">密码:</label>
        <div class="layui-input-block">
            <input type="text"  style="width:200px;" name="title" lay-verify="title" autocomplete="off" placeholder="请输入密码" class="layui-input">
        </div>
    </div>
</form>
</div>
<a href="choose.jsp"><button type="button" class="layui-btn layui-btn-normal">登录</button></a>
</body>
</html>
