# 检索推送配置集

请求
- Service Key - ranch.push.query
- URI - /push/query

参数

|名称|类型|说明|
|---|---|---|
|key|string|引用键，支持模糊匹配。|
|subject|string|标题，支持模糊匹配。|
|state|int|状态：-1-全部；0-待审核；1-使用中。|
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
      "key": "引用键",
      "subject": "标题",
      "content": "内容",
      "state": "状态：0-待审核；1-使用中",
      "time": "时间"
    }
  ]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
