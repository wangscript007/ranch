# 获取用户认证信息集

请求
- Service Key - ranch.user.auth.query
- URI - /user/auth/query

参数
- user 用户ID。

返回值
```text
[
    {
        "id": "ID值",
        "user": "用户ID",
        "uid": "认证ID",
        "type": "类型：0-机器码；1-自有账号；其他为第三方账号"
    }
]
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
