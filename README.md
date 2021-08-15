<h2>项目使用框架：基于Socket 和HTTP 的Web MVC自制框架</h2>
<h4>1.通过底层socket 实现TCP通信。</h4>

<h4>2.实现了HTTP报文的接受、解析、生成、返回。</h4>

<h4>3.采用MVC架构，控制层、显示层、数据层代码分离，代码便于复用，可拓展性强。<h4>
<ul>
<li>M层：自制了基于mysql的ORM。ORM基类实现了增删改查逻辑的封装，并且保存了所有的javaBean。</li>
<li>V层：模板使用了freemaker，也使用了使用字符串替换的拼接方式。</li>
<li>C层：实现了URI path到路由函数的映射，路由分发功能,和各种逻辑的处理。</li>
 </ul>
本项目为该框架的试用项目。
<ul>
<li>实现了基本的用户注册，登录。</li>
<li>个人主页，微博和评论的增删改查功能。</li>
<li>使用服务端Session实现状态保持。</li>
<li>密码加盐Hash存储。</li>
<h2><<<<<head</h2>
<h3>详细</h3>
<h2>主页</h2>
 <img src =https://github.com/PGmengqi/weibo/blob/master/img/%E4%B8%BB%E9%A1%B5.png>
 <h2>注册</h2>
<img src=https://github.com/PGmengqi/weibo/blob/master/img/%E6%B3%A8%E5%86%8C.png>
 <h2>登录</h2>
<img src=https://github.com/PGmengqi/weibo/blob/master/img/%E7%99%BB%E5%BD%95.png>
