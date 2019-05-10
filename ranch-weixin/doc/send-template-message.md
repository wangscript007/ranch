# 发送模板消息

请求
- Service Key - ranch.weixin.send-template-message
- URI - /weixin/send-template-message

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|string|是|引用key。|
|appId|string|否|APP ID。|
|receiver|string|是|接收者，可以是Open ID或Union ID。|
|url|string|否|跳转URL。|
|miniAppId|string|否|小程序APP ID。|
|miniPagePath|string|否|小程序页面路径。|
|data|string|是|模板数据，JSON格式的字符串数据。|
|color|string|否|颜色。|

返回
```
{}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)，签名密钥名为`ranch-weixin`。
