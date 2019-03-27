# 保存

请求
- Service Key - ranch.stripe.save
- URI - /stripe/save

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|char(100)|是|引用key。|
|name|char(100)|否|名称。|
|email|char(100)|是|Email。|
|publishable|string|是|发行号。|
|secret|string|是|密钥。|

返回值
```
""
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
