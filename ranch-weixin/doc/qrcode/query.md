# 检索

请求
- Service Key - ranch.weixin.qrcode.query
- URI - /weixin/qrcode/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|char(100)|否|微信key。|
|appId|char(100)|否|App ID。|
|user|char(36)|否|用户。|
|name|char(100)|否|名称，模糊匹配。|
|scene|char(100)|否|场景，模糊匹配。|
|time|char(100)|否|时间范围，以逗号分隔，格式`yyyy-MM-dd`或`yyyy-MM-dd HH:mm:ss`。|
|pageSize|int|否|每页显示记录数，默认20。|
|pageNum|int|否|当前显示页数。|

返回值
```json
{
    "count": "总记录数",
    "size": "每页显示记录数",
    "number": "当前显示页数",
    "list": [{
       "key": "微信key",
       "appId": "App ID",
       "user": "用户ID",
       "name": "名称",
       "scene": "场景",
       "ticket": "Ticket",
       "url": "微信URL",
       "time": "时间""
    }]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)，签名密钥名为`ranch-weixin`。
