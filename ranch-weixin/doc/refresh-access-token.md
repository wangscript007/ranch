# 刷新Access Token

请求
- Service Key - ranch.weixin.refresh-access-token
- URI - /weixin/refresh-access-token

参数

|名称|类型|说明|
|---|---|---|
|key|char(100)|引用key。|

返回
```
""
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)，签名密钥名为`ranch-weixin`。
