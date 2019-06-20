# 新增

请求
- Service Key - ranch.weixin.qrcode.create
- URI - /weixin/qrcode/create

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|char(100)|是|微信key。|
|user|char(36)|是|用户ID。|
|name|char(100)|是|名称。|
|scene|char(100)|是|场景。|

返回值
```
""
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)，签名密钥名为`ranch-weixin`。
