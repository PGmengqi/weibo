<!DOCTYPE html>
<html>
<!-- head 中是放一些控制信息, 不会被显示 -->
<head>
    <!-- meta charset 指定了页面编码, 否则中文会乱码 -->
    <meta charset="utf-8">
    <!-- title 是浏览器显示的页面标题 -->
    <title>todo</title>
</head>
<!-- body 中是浏览器要显示的内容 -->
<body>
<form action="/weibo/add" method="get">
    <textarea name="content" rows="8" cols="40"></textarea>
    <br/>
    <button type="submit">todo</button>
</form>
<div>
    <#list weibos as weibo>
        <h3>
            ${weibo.id}: ${weibo.content}
            <a href=/comment/new?id=${weibo.id}>添加评论</a>
            <a href=/todo/delete?id=${weibo.id}>删除</a>
            <a href=/todo/edit?id=${weibo.id}>编辑</a>
        </h3>
        <div>
        <#list weibo.comments as comment>
            ${comment.id}: ${comment.content}
        </#list>
        </div>
    </#list>

</div>
</body>
</html>
