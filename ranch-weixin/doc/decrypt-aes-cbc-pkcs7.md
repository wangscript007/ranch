# 小程序数据解密

请求
- Service Key - ranch.weixin.decrypt-aes-cbc-pkcs7
- URI - /weixin/decrypt-aes-cbc-pkcs7

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|char(100)|否|引用key，为空则使用缓存的SessionKey。|
|code|char(100)|否|微信认证code，为空则使用缓存的SessionKey。|
|iv|string|是|加密算法的初始向量。|
|message|string|是|加密数据。|

> 参数均为Base64编码数据。

返回解密后的数据，如果解密失败则返回空JSON。
