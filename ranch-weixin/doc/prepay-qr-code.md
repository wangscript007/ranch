# 生成支付二维码

请求
- Service Key - ranch.weixin.prepay-qr-code
- URI - /weixin/prepay-qr-code

参数

|名称|类型|说明|
|---|---|---|
|key|char(100)|引用key。|
|user|char(36)|用户ID。|
|subject|char(100)|订单名称。|
|amount|int|支付金额，单位：分。|
|notifyUrl|char(100)|异步通知URL地址。|
|size|int|二维码图片大小，小于等于0则使用默认值。|
|logo|string|LOGO图片名。|

返回值
```
byte[]
```
> 如果生成成功则返回二维码图片流。