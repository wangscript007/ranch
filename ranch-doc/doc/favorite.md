# 增减收藏数

请求
- Service Key - ranch.doc.favorite
- URI - /doc/favorite

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|char(36)|是|ID值。|
|favorite|int|是|收藏数：正数表示增加，负数表示减少。|

返回值
```text
""
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
