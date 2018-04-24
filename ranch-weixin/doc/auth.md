# 微信登入认证

请求
- Service Key - ranch.weixin.auth
- URI - /weixin/auth

参数

|名称|类型|说明|
|---|---|---|
|key|char(100)|引用key。|
|code|char(100)|微信认证code。|
|type|int|类型：0-公众号；1-小程序。|

返回值
```json
{}
```
> 如果认证成功，则返回微信用户信息，如：Open ID、Union ID、昵称、头像等信息。