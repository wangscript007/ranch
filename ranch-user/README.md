# 用户 & 认证

用户&认证模块主要实现统一的用户信息管理，及身份认证服务。

属性

|属性|类型|说明|
|---|---|---|
|password|char(32)|密码。|
|secret|char(32)|安全密码。|
|idcard|char(100)|身份证号。|
|name|char(100)|姓名。|
|nick|char(100)|昵称。|
|mobile|char(100)|手机号。|
|email|char(100)|Email地址。|
|portrait|char(100)|头像。|
|gender|int|性别：0-未知；1-男；2-女。|
|birthday|date|出生日期，格式yyyy-MM-dd。|
|inviter|char(36)|邀请人。|
|inviteCount|int|邀请人数。|
|code|char(8)|唯一编码。|
|register|datetime|注册时间，格式yyyy-MM-dd HH:mm:ss。|
|grade|int|等级：<50为用户；>=50为管理员；99为超级管理员。|
|state|int|状态：0-正常；1-禁用。|

> 密码/安全密码使用`md5(UserModel.NAME + sha1(password + UserModel.NAME))`。

[邀请人](doc/inviter.md)

[注册](doc/sign-up.md)

[登入](doc/sign-in.md)

[微信PC端登入跳转](doc/sign-in-wx-pc.md)

[获取当前登入用户信息](doc/sign.md)

[登出](doc/sign-out.md)

[修改当前用户信息](doc/modify.md)

[修改当前用户认证密码](doc/password.md)

[修改当前用户头像](doc/portrait.md)

[获取用户信息](doc/get.md)

[使用唯一编码获取用户信息](doc/find-by-code.md)

[使用UID获取用户信息](doc/find-by-uid.md)

[检索用户](doc/query.md)

[修改用户等级](doc/grade.md)

[修改用户状态](doc/state.md)

[获取认证信息集](doc/auth/query.md)

[删除认证](doc/auth/delte.md)

[获取在线用户信息集](doc/online/query.md)

[强制用户下线](doc/online/sign-out.md)
