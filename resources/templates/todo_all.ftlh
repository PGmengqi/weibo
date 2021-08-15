<!DOCTYPE html>
<html>
    <!-- head 中是放一些控制信息, 不会被显示 -->
    <head>
        <!-- meta charset 指定了页面编码, 否则中文会乱码 -->
        <meta charset="utf-8">
        <!-- title 是浏览器显示的页面标题 -->
        <title>todo</title>
        <link rel="stylesheet" href="/static?file=todo.css">
    </head>
    <!-- body 中是浏览器要显示的内容 -->
    <body>
        <form action="/todo/add" method="get">
            <textarea name="content" rows="8" cols="40"></textarea>
            <br/>
            <button type="submit">todo</button>
        </form>


        <svg viewBox="0 0 0 0" style="position: absolute; z-index: -1; opacity: 0;">
            <defs>
                <linearGradient id="boxGradient" gradientUnits="userSpaceOnUse" x1="0" y1="0" x2="25" y2="25">
                    <stop offset="0%"   stop-color="#27FDC7"/>
                    <stop offset="100%" stop-color="#0FC0F5"/>
                </linearGradient>

                <linearGradient id="lineGradient">
                    <stop offset="0%"    stop-color="#0FC0F5"/>
                    <stop offset="100%"  stop-color="#27FDC7"/>
                </linearGradient>

                <path id="todo__line" stroke="url(#lineGradient)" d="M21 12.3h168v0.1z"></path>
                <path id="todo__box" stroke="url(#boxGradient)" d="M21 12.7v5c0 1.3-1 2.3-2.3 2.3H8.3C7 20 6 19 6 17.7V7.3C6 6 7 5 8.3 5h10.4C20 5 21 6 21 7.3v5.4"></path>
                <path id="todo__check" stroke="url(#boxGradient)" d="M10 13l2 2 5-5"></path>
                <circle id="todo__circle" cx="13.5" cy="12.5" r="10"></circle>
            </defs>
        </svg>


        <div class="todo-list">

            <#list todos as todo>

                <label class="todo">
                    <input class="todo__state" type="checkbox" />

                    <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 200 25" class="todo__icon">
                        <use xlink:href="#todo__line" class="todo__line"></use>
                        <use xlink:href="#todo__box" class="todo__box"></use>
                        <use xlink:href="#todo__check" class="todo__check"></use>
                        <use xlink:href="#todo__circle" class="todo__circle"></use>
                    </svg>

                    <div class="todo__text"> ${todo.id}: ${todo.content}</div>

                    <a href=/todo/delete?id=${todo.id}>删除</a>
                    <a href=/todo/edit?id=${todo.id}>编辑</a>
                </label>
            </#list>
        </div>

        <script src="/static?file=todo.js"></script>
    </body>
</html>
