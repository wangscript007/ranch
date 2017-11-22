# 检索支付记录集

请求
- Service Key - ranch.payment.query
- URI - /payment/query

参数

|名称|类型|说明|
|---|---|---|
|type|string|类型，如果为空则为所有。|
|user|string|用户ID，如果为空则为所有用户。|
|orderNo|string|订单号，为空则不限制。|
|billNo|string|单据号，为空则不限制。|
|tradeNo|string|网关订单号，为空则不限制。|
|state|int|状态：-1-全部；0-新建；1-成功；2-失败。|
|start|string|开始日期，格式：yyyy-MM-dd，为空则不限制。|
|end|string|结束日期，格式：yyyy-MM-dd，为空则不限制。|
|pageSize|int|每页显示记录数，小于等于0则默认20。|
|pageNum|int|当前显示页数。|

返回值
```json
{
  "count": "总记录数",
  "size": "每页显示记录数",
  "number": "当前显示页数",
  "list": [
    {
      "type": "类型",
      "user": "用户ID",
      "amount": "金额，单位：分",
      "orderNo": "订单号",
      "billNo": "单据号",
      "tradeNo": "网关订单号",
      "state": "状态：0-新建；1-成功；2-失败",
      "notify": "通知URL地址",
      "start": "开始时间",
      "end": "结束时间"
    }
  ]
}
```

> 包含其他扩展属性。

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
