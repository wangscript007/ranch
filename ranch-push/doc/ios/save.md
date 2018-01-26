# 保存

请求
- Service Key - ranch.push.ios.save
- URI - /push/ios/save

参数

|名称|类型|说明|
|---|---|---|
|appCode|char(100)|APP编码。|
|p12|string|证书，BASE64编码。|
|password|char(100)|证书密码。|
|destination|int|目的地：0-开发；1-正式。|

返回值
```json
{
  "id": "ID值",
  "appCode": "APP编码",
  "p12": "证书，BASE64编码",
  "password": "证书密码",
  "destination": "目的地：0-开发；1-正式",
  "time": "更新时间"
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
