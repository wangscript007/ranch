# 保存配置

请求
- Service Key - ranch.alipay.save
- URI - /alipay/save

参数

|名称|类型|说明|
|---|---|---|
|key|char(100)|引用key，不可重复。|
|name|char(100)|名称。|
|appId|char(100)|APP ID，不可重复。|
|privateKey|char(100)|私钥。|
|publicKey|char(100)|公钥。|

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

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
