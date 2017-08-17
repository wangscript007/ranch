# 校验验证码

请求
- Service Key - ranch.captcha.validate
- URI - /captcha/validate

参数

|属性|类型|说明|
|---|---|---|
|key|char(100)|引用key，必须。|
|code|string|验证码，必须。|

返回值
```boolean
true/false
```

> 如果未设置验证码配置信息，即引用key不存在，则总是返回true。

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
