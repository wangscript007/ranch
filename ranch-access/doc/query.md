# 检索

请求
- Service Key - ranch.access.query
- URI - /access/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|host|string|否|站点。|
|uri|string|否|请求URI，模糊匹配后段。|
|user|string|否|用户ID或UID。|
|userAgent|string|否|User Agent，模糊匹配。|
|start|date|否|开始时间，格式：yyyy-MM-dd。|
|end|date|否|结束时间，格式：yyyy-MM-dd。|
|pageSize|int|否|每页显示记录数，小于等于0则默认20。|
|pageNum|int|否|当前显示页数。|


返回值
```json
{
    "count": "总记录数",
    "size": "每页显示记录数",
    "number": "当前显示页数",
    "list": [
        {
            "host": "站点",
            "uri": "请求URI",
            "user": "用户ID",
            "ip": "IP",
            "userAgent": "User Agent",
            "referer": "引用",
            "header": "头信息集",
            "time": "时间"
        }
    ]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
