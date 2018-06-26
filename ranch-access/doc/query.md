# 检索

请求
- Service Key - ranch.access.query
- URI - /access/query

参数

|名称|类型|必须|说明|
|host|char(255)|否|站点。|
|uri|char(255)|否|请求URI，模糊匹配后段。|
|user|char(36)|否|用户ID或UID。|
|userAgent|char(255)|否|User Agent，模糊匹配。|
|start|datetime|否|开始时间，格式：yyyy-MM-dd。|
|end|datetime|否|结束时间，格式：yyyy-MM-dd。|
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
