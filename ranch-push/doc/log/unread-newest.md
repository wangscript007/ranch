# 最新未读

获取最新一条未读日志，如果无未读日志则返回最新一条已读日志；如果没有任何日志则返回空JSON。

请求
- Service Key - ranch.push.log.unread-newest
- URI - /push/log/unread-newest

参数

|名称|类型|说明|
|---|---|---|
|user|char(36)|用户ID。|
|appCode|char(100)|APP编码。|

返回值
```json
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
```
