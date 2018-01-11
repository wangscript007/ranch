# 推送

请求
- Service Key - ranch.push.send
- URI - /push/send

参数

|名称|类型|说明|
|---|---|---|
|key|string|引用键。|
|receiver|string|接收账号：SMTP为Email，短信为手机号。|
|args|string|配置参数集。|

返回值
```
true or false
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
