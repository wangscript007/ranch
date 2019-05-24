# 检索

请求
- Service Key - ranch.weixin.reply.query
- URI - /weixin/reply/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|char(100)|否|引用key。|
|receiveType|char(100)|否|接收类型。|
|receiveMessage|char(100)|否|接收消息。|
|state|int|否|状态：-1-全部；0-待使用；1-使用中。|
|pageSize|int|否|每页显示记录数，默认20。|
|pageNum|int|否|当前显示页数。|

返回值
```json
{
    "count": "总记录数",
    "size": "每页显示记录数",
    "number": "当前显示页数",
    "list": [{
        "key": "引用KEY",
        "sort": "顺序",
        "receiveType": "接收类型",
        "receiveMessage": "接收消息",
        "sendType": "发送类型",
        "sendAppId": "小程序APP ID",
        "sendMessage": "发送消息",
        "sendTitle": "发送标题",
        "sendDescription": "发送描述",
        "sendUrl": "发送链接",
        "sendPicurl": "发送图片链接",
        "state": "状态：0-待使用；1-使用中"
    }]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)，签名密钥名为`ranch-weixin`。
