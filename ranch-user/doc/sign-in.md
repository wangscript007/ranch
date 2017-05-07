# 登入

请求
- Service Key - ranch.user.sign-in
- URI - /user/sign-in

参数
- uid UID值：type=0则uid为MacID；type=1则uid为用户名（手机号、Email、账号等）；type=2则uid为微信认证code。
- password 密码，如果为第三方认证类型则password为第三方AppID，为空表示使用默认AppID。
- macId 客户端机器码。
- type 认证类型：0-机器码；1-自有账号；2-微信。

返回值
```json
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

> 如果type>1（第三方账号）登入时如果账户信息不存在，将自动创建（注册）新账户。
