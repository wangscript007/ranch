# 推送到用户

请求
- Service Key - ranch.push.send-user
- URI - /push/send-user

参数

|名称|类型|必须|说明|
|---|---|---|---|
|user|string|是|用户ID。|
|appCode|string|是|设备APP CODE。|
|subject|string|否|标题。|
|content|string|否|内容。|
|args|string|否|配置参数集，JSON格式。|

返回值
```
true or false
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
