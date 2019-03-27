# 检索

请求
- Service Key - ranch.stripe.transaction.query
- URI - /stripe/transaction/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|user|char(36)|否|用户ID。|
|create|string|否|创建时间范围，以逗号分隔，格式：yyyy-MM-dd或yyyy-MM-dd HH:mm:ss。|
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
      "user": "用户ID",
      "status": "状态",
      "request": "请求数据",
      "response": "返回数据",
      "time": "时间"
    }
  ]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
