# 微信小程序数据解密

请求
- Service Key - ranch.weixin.decrypt-aes-cbc-pkcs7
- URI - /weixin/decrypt-aes-cbc-pkcs7

参数

|名称|类型|说明|
|---|---|---|
|sessionKey|string|Session key。|
|iv|string|iv参数。|
|message|string|加密数据。|

> 参数均为Base64编码数据。

返回值
```json
{}
```
