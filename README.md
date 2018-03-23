# 健身头条
本项目是一个资讯分享的健身头条网站，用户注册后可以发布资讯，也可以评论资讯或者对资讯进行点赞。主要包括以下模块：用户管理模块、资讯管理模块、评论管理模块、站内信模块、异步队列模块、文件上传下载模块等。

技术细节：

1.后台开发框架采用spring boot + mybatis + redis，前端采用velocity模板进行页面开发。采用mysql数据库对实体信息进行存储，redis用在缓存、点赞等相关方面。

2.使用异步队列设计来完成对站内信、登录异常、点赞问题等事件的处理，提升系统的用户交互体验，采用邮件发送技术对用户关心的资讯进行邮件通知。

3.采用七牛云对象存储，通过提供的SDK进行对资讯中的图片、用户分享的文件进行云上传、下载。

4.对资讯和评论的发布进行敏感词过滤。(未实现)

[项目展示](http://bxgfhg.club)
     

