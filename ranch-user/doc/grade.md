# 修改用户等级

请求
- Service Key - ranch.user.grade
- URI - /user/grade

参数

|名称|类型|说明|
|---|---|---|
|id|char(36)|用户ID值。|
|grade|int|等级：<50为用户；>=50为管理员；99为超级管理员。|

返回值
```text
""
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
