# 检索

请求
- Service Key - ranch.popular.query
- URI - /popular/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|char(100)|否|引用键，支持模糊匹配。|
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
      "key": "引用key",
      "value": "值",
      "count": "次数"
    }
  ]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
