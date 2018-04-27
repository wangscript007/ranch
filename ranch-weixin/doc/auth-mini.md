# 小程序登入认证

请求
- Service Key - ranch.weixin.auth-mini
- URI - /weixin/auth-mini

参数

|名称|类型|说明|
|---|---|---|
|key|char(100)|引用key。|
|code|char(100)|微信认证code。|
|iv|string|用户授权信息加密算法的初始向量。|
|message|string|用户授权信息。|

> 如果`iv` & `message`均不为空，则解密用户授权信息，并返回。

返回值
```json
{}
```
> 如果认证成功，则返回微信用户信息，如：Open ID、Union ID、昵称、头像等信息。