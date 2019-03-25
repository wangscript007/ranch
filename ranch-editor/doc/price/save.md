# 保存

请求
- Service Key - ranch.editor.price.save
- URI - /editor/price/save

参数

|名称|类型|必须|说明|
|---|---|---|---|
|type|char(100)|否|类型，为空表示全部。|
|group|char(100)|否|分组，为空表示全部。|
|amount|int|否|价格，单位：分。|
|vip|int|否|VIP价格，单位：分。|
|limited|int|否|限时价格，单位：分。|
|time|datetime|否|限时时间。|

返回值
```
""
```

> 保存后会自动[设置模板价格](../price.md)。

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
