# 发起PC WEB端支付

请求
- Service Key - ranch.alipay.fast-instant-trade-pay
- URI - /alipay/fast-instant-trade-pay

参数

|名称|类型|说明|
|---|---|---|
|key|char(100)|引用key，必须。|
|user|char(36)|用户ID，为空则使用当前用户ID。|
|subject|string|交易说明，必须。|
|amount|int|金额，单位：分。|
|notice|string|[通知配置](notice.md)。|
|returnUrl|char(100)|支付完成（成功/失败）同步通知地址。|

返回值
```html
"页面支付表单HTML代码"
```
