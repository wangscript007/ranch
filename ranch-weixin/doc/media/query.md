# 检索

请求
- Service Key - ranch.weixin.media.query
- URI - /weixin/media/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|char(100)|否|微信key。|
|appId|char(100)|否|App ID。|
|type|char(100)|否|类型：image-图片；voice-语音；video-视频；thumb-缩略图。|
|name|char(100)|否|名称，模糊匹配。|
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
       "type": "类型：image-图片；voice-语音；video-视频；thumb-缩略图",
       "name": "名称",
       "mediaId": "媒体ID",
       "url": "微信URL",
       "uri": "文件URI",
       "time": "时间"
    }]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)，签名密钥名为`ranch-weixin`。
