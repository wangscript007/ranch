# 充值

请求
- Service Key - ranch.stripe.transaction.charge
- URI - /stripe/transaction/charge

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|string|是|引用key。|
|amount|int|是|金额，单位：分。|
|currency|string|是|币种。|
|tradeNo|string|是|订单ID，Stripe Token。|

返回值
```json
{}
```

> 返回数据为Stripe检验结果。
