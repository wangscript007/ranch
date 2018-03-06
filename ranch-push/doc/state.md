# 修改状态

请求
- Service Key - ranch.push.state
- URI - /push/state

参数

|名称|类型|说明|
|---|---|---|
|id|char(36)|ID值。|
|state|int|状态：0-待审核；1-使用中。|

返回值
```json
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
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
