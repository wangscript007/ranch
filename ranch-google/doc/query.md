# 检索

请求
- Service Key - ranch.google.query
- URI - /google/query

返回值
```json
[{
    "id": "ID值",
    "key": "引用key",
    "name": "名称",
    "clientId": "客户端ID"
}]
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
