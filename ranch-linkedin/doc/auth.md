# 认证

请求
- Service Key - ranch.linkedin.auth
- URI - /linkedin/auth

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|char(100)|是|引用key。|
|code|string|是|认证code。|
|redirectUri|string|是|转发URI。|

返回值
```json
{}
```

> 返回值为Linkedin认证结果。
