# 订单完成

请求
- Service Key - ranch.transfer.complete
- URI - /transfer/complete

参数

|名称|类型|说明|
|---|---|---|
|orderNo|char(21)|类型。|
|amount|int|金额，单位：分。|
|tradeNo|char(100)|第三方订单号。|
|state|int|状态：1-成功；2-失败。|

> 其TA扩展属性将被保存到json属性的complete中。

返回值
```json
{
  "type": "类型",
  "appId": "网关APP ID",
  "user": "用户ID",
  "amount": "金额，单位：分",
  "orderNo": "订单号",
  "billNo": "单据号",
  "tradeNo": "网关订单号",
  "state": "状态：0-新建；1-成功；2-失败",
  "notice": "通知配置",
  "start": "开始时间",
  "end": "结束时间"
}
```

> 包含其他扩展属性。
