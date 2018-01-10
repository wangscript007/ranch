# 保存配置

请求
- Service Key - ranch.push.save
- URI - /push/save

参数

|名称|类型|说明|
|---|---|---|
|key|char(100)|引用键。|
|sender|char(100)|推送器，可选值见下表。|
|subject|char(100)|标题。|
|content|char(100)|内容。|
|template|char(100)|模板ID。|
|name|char(100)|发送者名称。|
|state|int|状态：0-待审核；1-使用中。|

返回值
```json
{
  "id": "ID值",
  "key": "引用key",
  "name": "名称",
  "appId": "APP ID",
  "privateKey": "私钥",
  "publicKey": "公钥"
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)，签名密钥名为`ranch-alipay`。
