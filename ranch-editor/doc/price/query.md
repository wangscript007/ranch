# 检索

请求
- Service Key - ranch.editor.price.query
- URI - /editor/price/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|type|string|否|类型，为空表示不限制。|
|group|string|否|分组，为空表示不限制。|
|pageSize|int|否|每页显示记录数，默认20。|
|pageNum|int|否|当前显示页数。|

返回值
```json
{
    "count": "总记录数",
    "size": "每页显示记录数",
    "number": "当前显示页数",
    "list": [
        {
            "type": "类型",
            "group": "分组",
            "amount": "价格，单位：分",
            "vip": "VIP价格，单位：分",
            "limited": "限时价格，单位：分",
            "time": "限时时间"
        }
    ]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
