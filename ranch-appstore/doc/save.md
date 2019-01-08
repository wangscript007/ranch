# 保存

请求
- Service Key - ranch.appstore.save
- URI - /appstore/save

参数

|名称|类型|必须|说明|
|---|---|---|---|
|productId|char(100)|是|产品ID。|
|name|char(100)|否|名称。|
|amount|int|是|金额，单位：分。|

返回值
```
""
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
