# 发送多人

请求
- Service Key - ranch.notice.sends
- URI - /notice/sends

参数

|名称|类型|必须|说明|
|---|---|---|---|
|users|strin|是|用户ID集，多个间以逗号分隔。|
|type|char(100)|否|类型。|
|subject|char(100)|否|标题。|
|content|string|是|内容。|
|link|string|否|链接。|

返回值
```
""
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
