# 登入

请求
- Service Key - ranch.user.sign-in
- URI - /user/sign-in

参数

|名称|类型|必须|说明|
|---|---|---|---|
|uid|char(100)|是|UID值：type=0则uid为用户名（手机号、Email、账号等）；type=1或2则uid为微信认证code。|
|password|string|是|密码，如果为第三方认证类型则password为第三方配置key。|
|type|int|是|认证类型：0-自有账号；1-微信公众号；2-微信小程序。|
|iv|string|否|微信小程序用户授权信息加密算法的初始向量。|
|message|string|否|微信小程序用户授权信息。|

> 如果`iv` & `message`均不为空，则解密用户授权信息，并返回。

返回值
```json
{
    "id": "ID值",
    "name": "姓名",
    "nick": "昵称",
    "mobile": "手机号",
    "email": "Email地址",
    "portrait": "头像",
    "gender": "性别：0-未知；1-男；2-女",
    "address": "详细地址",
    "birthday": "出生日期",
    "code": "唯一编码",
    "register": "注册时间",
    "grade": "等级：<50为用户；>=50为管理员；99为超级管理员",
    "state": "状态：0-正常；1-禁用",
    "auth3": "第三方认证信息，使用第三方认证登入时返回"
}
```

> 当type>1（第三方账号）登入时如果账户信息不存在，将自动创建（注册）新账户。
