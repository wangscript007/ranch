# 检索

请求
- Service Key - ranch.appstore.receipt.query
- URI - /appstore/receipt/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|productId|char(100)|否|产品ID。|
|time|string|否|时间范围，以逗号分隔，格式：yyyy-MM-dd或yyyy-MM-dd HH:mm:ss。|
|pageSize|int|否|每页显示记录数，小于等于0则默认20。|
|pageNum|int|否|当前显示页数。|

返回值
```json
{
  "count": "总记录数",
  "size": "每页显示记录数",
  "number": "当前显示页数",
  "list": [
    {
      "id": "ID值",
      "productId": "产品ID",
      "price": "价格，单位：分",
      "quantity": "数量",
      "amount": "金额，单位：分",
      "status": "状态",
      "md5": "请求MD5",
      "request": "请求数据",
      "response": "返回数据",
      "time": "时间"
    }
  ]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
