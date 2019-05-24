## 微信

微信模块主要实现微信接口适配。

属性

|属性|类型|说明|
|---|---|---|
|key|char(100)|引用key，不可重复。|
|name|char(100)|名称。|
|appId|char(100)|APP ID，不可重复。|
|secret|char(100)|密钥。|
|token|char(100)|验证Token。|
|mchId|char(100)|商户ID。|
|mchKey|char(100)|商户密钥。|
|accessToken|char(100)|当前Access Token。|
|jsapiTicket|char(100)|当前Jsapi Ticket。|
|time|timestamp|更新时间。|

[检索配置集](doc/query.md)

[保存配置](doc/save.md)

[刷新Access Token](doc/refresh-access-token.md)

[创建公众号菜单](doc/menu.md)

[删除配置](doc/delete.md)

[接收微信通知](doc/wx.+.md)

[获取APP ID](doc/app-id.md)

[关注并登入](doc/subscribe-sign-in.md)

[登入认证](doc/auth.md)

[小程序登入认证](doc/auth-mini.md)

[生成支付二维码](doc/prepay-qr-code.md)

[APP预支付](doc/prepay-app.md)

[小程序预支付](doc/prepay-mini.md)

[小程序数据解密](doc/decrypt-aes-cbc-pkcs7.md)

[获取二维码](doc/wxa-code-unlimit.md)

[获取JS API Ticket签名](doc/jsapi-ticket-signature.md)

[发送模板消息](doc/send-template-message.md)

## 回复

属性

|属性|类型|说明|
|---|---|---|
|key|char(100)|引用KEY。|
|sort|int|顺序。|
|receiveType|char(100)|接收类型。|
|receiveMessage|char(100)|接收消息。|
|sendType|char(100)|发送类型：text-文本；image-图片；mpnews-图文；news-图文（外链）。|
|sendMessage|string|发送消息。|
|sendTitle|char(100)|发送标题。|
|sendDescription|char(100)|发送描述。|
|sendUrl|char(100)|发送链接。|
|sendPicurl|char(100)|发送图片链接。|
|state|int|状态：0-待使用；1-使用中。|

接收类型

|值|说明|
|---|---|
|event|事件，如关注、扫码等。|
|text|文本，用户在公众号输入文本信息。|

事件消息

|值|说明|
|---|---|
|subscribe|关注。|
|SCAN|已关注扫码。|
|unsubscribe|取消关注。|

发送类型

|值|公众号|小程序|说明|
|---|---|---|---|
|text|是|是|文本。|
|image|是|是|图片。|
|mpnews|是|否|图文。|
|news|是|否|图文（外链）。|
|link|否|是|图文链接。|
|miniprogrampage|否|是|卡片。|

[检索](doc/reply/query.md)

[保存](doc/reply/save.md)

[删除](doc/reply/delete.md)

## 模板消息

属性

|属性|类型|说明|
|---|---|---|
|key|char(100)|引用key。|
|weixinKey|char(100)|微信key。|
|type|int|类型：0-公众号；1-小程序。|
|templateId|char(100)|模板ID。|
|url|char(100)|跳转URL。|
|page|char(100)|小程序页面。|
|miniAppId|char(100)|小程序APP ID。|
|color|char(100)|字体颜色。|
|keyword|char(100)|放大关键词。|
|state|int|状态：0-待审核；1-已上线。|

[检索](doc/template/query.md)

[保存](doc/template/save.md)

[发送](doc/template/send.md)

[删除](doc/template/delete.md)
