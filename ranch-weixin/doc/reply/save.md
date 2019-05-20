# 保存

请求
- Service Key - ranch.weixin.reply.save
- URI - /weixin/reply/save

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|char(36)|否|ID值，不存在则新增；存在则修改。|
|key|char(100)|是|引用KEY，与微信配置一致。|
|sort|int|否|顺序。|
|receiveType|char(100)|是|接收类型。|
|receiveMessage|char(100)|是|接收消息。|
|sendType|char(100)|是|发送类型。|
|sendMessage|string|是|发送消息。|
|sendTitle|char(100)|否|发送标题。|
|sendDescription|char(100)|否|发送描述。|
|sendUrl|char(100)|否|发送链接。|
|sendPicul|char(100)|否|发送图片链接。|
|state|char(100)|否|状态：0-待使用；1-使用中。|

返回值
```
""
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)，签名密钥名为`ranch-weixin`。
