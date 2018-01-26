# 保存

请求
- Service Key - ranch.push.aliyun.save
- URI - /push/aliyun/save

参数

|名称|类型|说明|
|---|---|---|
|appCode|char(100)|APP编码。|
|keyId|char(100)|KEY ID。|
|keySecret|char(100)|KEY密钥。|
|appKey|char(100)|APP KEY。|

返回值
```json
{
  "id": "ID值",
  "appCode": "APP编码",
  "keyId": "KEY ID",
  "keySecret": "KEY密钥",
  "appKey": "APP KEY",
  "time": "更新时间"
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
