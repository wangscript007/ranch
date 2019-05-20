# 保存

请求
- Service Key - ranch.weixin.template.send
- URI - /weixin/template/send

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|char(100)|是|引用key。|
|receiver|string|是|接收者。|
|formId|string|是|Form ID。|
|data|string|否|参数，JSON格式字符串。|

返回值
```
""
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)，签名密钥名为`ranch-weixin`。
