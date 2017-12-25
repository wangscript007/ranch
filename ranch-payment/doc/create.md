# 创建订单

请求
- Service Key - ranch.payment.create
- URI - /payment/create

参数

|名称|类型|说明|
|---|---|---|
|type|char(100)|类型。|
|appId|char(100)|网关APP ID。|
|user|char(36)|用户ID，如果为空则使用当前用户ID。|
|amount|int|金额，单位：分。|
|billNo|char(100)|单据号。|
|notice|string|订单完成（成功/失败）通知配置。|

> 其TA扩展属性将被保存到json属性的create中。

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

## 通知配置

通知配置为JSON格式的数据，支持微服务和HTTP两种方式，并且允许两种方式同时通知：

```json
{
  "service": "微服务key",
  "http": "HTTP(S)请求URL地址",
  "params": "通知参数，通知时将原样推送给接收方"
}
```