# 获取当前登入用户信息

请求
- Service Key - ranch.user.sign
- URI - /user/sign

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
    "introducer": "介绍人",
    "introduceCount": "介绍人数",
    "code": "唯一编码",
    "register": "注册时间",
    "grade": "等级：<50为用户；>=50为管理员；99为超级管理员",
    "state": "状态：0-正常；1-禁用",
    "auth3": "第三方认证信息，使用第三方认证登入时返回"
}
```

> 如果用户尚未登入或登入已失效，则返回空JSON“{}”。
