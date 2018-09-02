# 检索推送配置集

请求
- Service Key - ranch.push.query
- URI - /push/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|char(100)|否|引用键，支持模糊匹配。|
|sender|char(100)|否|推送器。|
|subject|char(100)|否|标题，支持模糊匹配。|
|state|int|否|状态：-1-所有；0-待审核；1-使用中。|
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
      "key": "引用键",
      "sender": "推送器",
      "appCode": "APP编码",
      "subject": "标题",
      "content": "内容",
      "template": "模板",
      "name": "发送者名称",
      "args": "默认参数集",
      "state": "状态：0-待审核；1-使用中",
      "time": "最后更新时间"
    }
  ]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
