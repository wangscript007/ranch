# 保存

请求
- Service Key - ranch.aliyun.save
- URI - /aliyun/save

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|char(100)|是|引用key。|
|name|char(100)|否|名称。|
|regionId|char(100)|是|接入区域。|
|accessKeyId|char(100)|是|访问key ID。|
|accessKeySecret|char(100)|是|访问key密钥。|

返回值
```json
{
  "id": "ID值",
  "key": "引用key",
  "name": "名称",
  "regionId": "接入区域",
  "accessKeyId": "访问key ID",
  "accessKeySecret": "访问key密钥"
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
