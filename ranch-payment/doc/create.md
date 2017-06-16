# 创建订单

请求
- Service Key - ranch.payment.create
- URI - /payment/create

参数

|名称|类型|说明|
|---|---|---|
|type|char(100)|类型。|
|user|char(36)|用户ID，如果为空则使用当前用户ID。|
|amount|int|金额，单位：分。|
|notify|char(100)|订单完成（成功/失败）通知URL地址。|

> 其TA扩展属性将被保存到json属性的create中。

返回值
```text
{
  "type": "类型",
  "user": "用户ID",
  "amount": "金额，单位：分",
  "orderNo": "订单号",
  "tradeNo": "网关订单号",
  "state": "状态：0-新建；1-成功；2-失败",
  "notify": "通知URL地址",
  "start": "开始时间",
  "end": "结束时间"
}
```

> 包含其他扩展属性。