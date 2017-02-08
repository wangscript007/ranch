# 注册

请求：
- Service Key - ranch.user.sign-up
- URI - /user/sign-up

参数
- uid UID值，type=0则uid为MacID，type=1则uid为用户名（手机号、Email、账号等），其他则为第三方OpenID。
- password 密码。
- type 认证类型：0-机器码；1-自有账号；其他为第三方账号。

返回值
```text
{
    "id": "ID值",
    "name": "姓名",
    "nick": "昵称",
    "mobile": "手机号",
    "email": "Email地址",
    "portrait": "头像",
    "gender": "性别：0-未知；1-男；2-女",
    "address": "详细地址",
    "birthday": "出生日期",
    "code": "唯一编码",
    "register": "注册时间",
    "grade": "等级：<50为用户；>=50为管理员；99为超级管理员",
    "state": "状态：0-正常；1-禁用"
}
```
