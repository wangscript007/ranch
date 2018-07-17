# 获取文档源内容

请求
- Service Key - ranch.doc.source
- URI - /doc/source

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|char(36)|是|ID值。|

返回值
```text
"source"
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
