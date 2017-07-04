# 强制用户下线

请求
- Service Key - ranch.user.online.sign-out
- URI - /user/online/sign-out

参数
- user 用户ID。
- uid 认证ID，仅当user为空时有效。
- ip IP地址。

返回值
```json
""
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
