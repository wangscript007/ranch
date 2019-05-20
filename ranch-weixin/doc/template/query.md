# 检索

请求
- Service Key - ranch.weixin.template.query
- URI - /weixin/template/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|char(100)|否|引用key。|
|weixinKey|char(100)|否|微信key。|
|type|char(100)|否|类型：-1-全部；0-公众号；1-小程序。|
|templateId|char(100)|否|模板ID。|
|state|int|否|状态：-1-全部；0-待审核；1-已上线。|
|pageSize|int|否|每页显示记录数，默认20。|
|pageNum|int|否|当前显示页数。|

返回值
```json
{
    "count": "总记录数",
    "size": "每页显示记录数",
    "number": "当前显示页数",
    "list": [{
        "key": "引用key",
        "weixinKey": "微信key",
        "type": "类型：0-公众号；1-小程序",
        "templateId": "模板ID",
        "url": "跳转URL",
        "page": "小程序页面",
        "miniAppId": "小程序APP ID",
        "color": "字体颜色",
        "keyword": "放大关键词",
        "state": "状态：0-待审核；1-已上线"
    }]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)，签名密钥名为`ranch-weixin`。
