# 检索

请求
- Service Key - ranch.ad.query
- URI - /ad/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|type|string|否|类型，为空则表示全部。|
|state|int|否|状态：-1-全部；0-下线；1-上线。|
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
            "sort": "顺序",
            "name": "名称",
            "resource": "资源地址",
            "operation": "操作",
            "target": "目标地址",
            "state": "状态：0-下线；1-上线"
        }
    ]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
