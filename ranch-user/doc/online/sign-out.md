# 强制用户下线

请求
- Service Key - ranch.user.online.sign-out
- URI - /user/online/sign-out

参数

|名称|类型|说明|
|---|---|---|
|user|char(36)|用户ID。|
|uid|char(100)|认证ID，仅当user为空时有效。|
|ip|char(100)|IP地址。|

返回值
```json
""
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
