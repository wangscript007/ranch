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
