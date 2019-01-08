# 检索

请求
- Service Key - ranch.appstore.query
- URI - /appstore/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
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
      "name": "名称",
      "amount": "金额，单位：分"
    }
  ]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
