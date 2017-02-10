# 修改用户状态

请求：
- Service Key - ranch.user.state
- URI - /user/state

参数
- id 用户ID值。
- state 状态：0-正常；1-禁用。

返回值
```text
""
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
