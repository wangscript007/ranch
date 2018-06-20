# 删除

请求
- Service Key - ranch.user.auth.delete
- URI - /user/auth/delete

参数

|名称|类型|必须|说明|
|---|---|---|---|
|uid|char(100)|是|UID值：type=0则uid为用户名（手机号、Email、账号等）；type=1或2则uid为微信认证code。|
|password|string|是|密码，如果为第三方认证类型则password为第三方配置key。|
|type|int|是|认证类型：0-自有账号；1-微信公众号；2-微信小程序。|
|iv|string|否|微信小程序用户授权信息加密算法的初始向量。|
|message|string|否|微信小程序用户授权信息。|

> 如果`iv` & `message`均不为空，则解密用户授权信息。

返回值
```
""
```
