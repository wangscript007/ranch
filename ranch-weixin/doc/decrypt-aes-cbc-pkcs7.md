# 小程序数据解密

请求
- Service Key - ranch.weixin.decrypt-aes-cbc-pkcs7
- URI - /weixin/decrypt-aes-cbc-pkcs7

参数

|名称|类型|说明|
|---|---|---|
|iv|string|加密算法的初始向量。|
|message|string|加密数据。|

> 参数均为Base64编码数据。

返回解密后的数据，如果解密失败则返回空JSON。
