# 修改当前用户信息

请求
- Service Key - ranch.user.modify
- URI - /user/modify

参数
- name 姓名。
- nick 昵称。
- mobile 手机号。
- email Email地址。
- gender 性别：0-未知；1-男；2-女。
- address 详细地址。
- birthday 出生日期。

> 参数未提供（is null）则表示不修改，保留原值。

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
