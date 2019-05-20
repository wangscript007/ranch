# 保存

请求
- Service Key - ranch.weixin.template.save
- URI - /weixin/template/save

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|char(100)|是|引用key。|
|weixinKey|char(100)|是|微信key。|
|type|int|否|类型：0-公众号；1-小程序。|
|templateId|char(100)|是|模板ID。|
|url|char(100)|否|跳转URL。|
|page|char(100)|否|小程序页面。|
|miniAppId|char(100)|否|小程序APP ID。|
|color|char(100)|否|字体颜色。|
|keyword|char(100)|否|放大关键词。|
|state|int|否|状态：0-待审核；1-已上线。|

返回值
```
""
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)，签名密钥名为`ranch-weixin`。
