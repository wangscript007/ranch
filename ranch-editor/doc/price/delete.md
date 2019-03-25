# 删除

请求
- Service Key - ranch.editor.price.delete
- URI - /editor/price/delete

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|string|是|ID值。|

返回值
```
""
```

> 删除操作不会修改模板价格。

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
