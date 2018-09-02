# 保存配置

请求
- Service Key - ranch.push.save
- URI - /push/save

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|char(36)|否|ID值。|
|key|char(100)|是|引用键。|
|sender|char(100)|是|推送器。|
|appCode|char(100)|否|APP编码。|
|subject|char(100)|否|标题。|
|content|strng|否|内容。|
|template|char(100)|否|模板ID。|
|name|char(100)|否|发送者名称。|
|args|string|否|默认参数集，JSON格式。|
|state|int|否|状态：0-待审核；1-使用中。|

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
