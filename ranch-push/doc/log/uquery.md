# 检索个人

请求
- Service Key - ranch.push.log.uquery
- URI - /push/log/uquery

参数

|名称|类型|说明|
|---|---|---|
|user|char(36)|用户ID。|
|appCode|char(100)|APP编码。|
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
