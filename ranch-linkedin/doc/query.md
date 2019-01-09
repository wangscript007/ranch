# 检索

请求
- Service Key - ranch.linkedin.query
- URI - /linkedin/query

返回值
```json
[{
    "id": "ID值",
    "key": "引用key",
    "name": "名称",
    "appId": "APP ID",
    "secret": "密钥""
}]
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
