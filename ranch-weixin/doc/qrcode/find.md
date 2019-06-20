# 查找

请求
- Service Key - ranch.weixin.qrcode.find
- URI - /weixin/qrcode/find

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|char(100)|是|微信key。|
|user|char(36)|是|用户ID。|
|name|char(100)|是|名称。|

返回值
```json
{
    "key": "微信key",
    "appId": "App ID",
    "user": "用户ID",
    "name": "名称",
    "scene": "场景",
    "ticket": "Ticket",
    "url": "微信URL",
    "time": "时间""
}
```
