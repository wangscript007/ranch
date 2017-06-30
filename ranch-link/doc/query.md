# 检索关联集

请求
- Service Key - ranch.link.query
- URI - /link/query

参数

|名称|类型|说明|
|---|---|---|
|type|char(100)|类型，必须。|
|id1|char(36)|关联ID1，如果为空则检索ID2，不为空则检索ID1。|
|id2|char(36)|关联ID2。|

返回值
```json
{
  "count": "总记录数",
  "size": "每页显示记录数",
  "number": "当前显示页数",
  "list": [
    {
      "type": "类型",
      "id1": "关联ID1",
      "id2": "关联ID2",
      "time": "更新时间"
    }
  ]
}
```
> 包含自定义属性。
