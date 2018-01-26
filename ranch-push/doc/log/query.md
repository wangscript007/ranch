# 检索

请求
- Service Key - ranch.push.log.query
- URI - /push/log/query

参数

|名称|类型|说明|
|---|---|---|
|user|char(36)|用户，为空则表示不限制。|
|receiver|char(100)|接收地址，为空则表示不限制。|
|appCode|char(100)|APP编码，为空则表示不限制。|
|sender|char(100)|推送器，为空则表示不限制。|
|start|string|开始日期，格式：yyyy-MM-dd；为空则表示不限制。|
|end|string|结束日期，格式：yyyy-MM-dd；为空则表示不限制。|
|state|int|状态：-1-全部；0-新建；1-已推送；2-已阅读；3-推送失败。|
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
      "id": "ID值",
      "user": "用户信息",
      "receiver": "接收地址",
      "appCode": "APP编码",
      "sender": "推送器",
      "push": "推送配置",
      "args": "参数集",
      "time": "时间",
      "state": "状态：0-新建；1-已推送；2-已阅读；3-推送失败"
    }
  ]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
