# 校验SVG

请求
- Service Key - ranch.resource.svg
- URI - /resource/svg

参数

|名称|类型|必须|说明|
|---|---|---|---|
|string|string|否|SVG文档，如果不为空则优先使用。|
|base64|string|否|Base64编码的SVG文档，当string参数为空时使用。|

返回值
```
true/false
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。