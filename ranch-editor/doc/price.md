# 定价

请求
- Service Key - ranch.editor.price
- URI - /editor/price

参数

|名称|类型|必须|说明|
|---|---|---|---|
|ids|string|是|ID集，以逗号分隔。|
|type|char(100)|类型。|
|group|char(100)|分组。|
|price|int|价格，单位：分。|
|vipPrice|int|VIP价格，单位：分。|
|limitedPrice|int|限时价格，单位：分。|
|limitedTime|datetime|限时时间，格式：yyyy-MM-dd HH:mm:ss。|

返回值
```
""
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
