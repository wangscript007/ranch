# 小程序预支付

请求
- Service Key - ranch.weixin.prepay-mini
- URI - /weixin/prepay-mini

参数

|名称|类型|说明|
|---|---|---|
|key|char(100)|引用key。|
|user|char(36)|用户ID。|
|subject|char(100)|订单名称。|
|amount|int|支付金额，单位：分。|
|billNo|char(100)|单据号。|
|notice|string|通知。|

返回值
```json
{
    "appid": "",
    "partnerid": "",
    "prepayid": "",
    "package": "Sign=WXPay",
    "noncestr": "",
    "timestamp": ""
}
```
