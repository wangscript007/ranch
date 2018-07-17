# 注册

请求
- Service Key - ranch.user.sign-up
- URI - /user/sign-up

参数

|名称|类型|必须|说明|
|---|---|---|---|
|uid|char(100)|是|UID值，type=0则uid为用户名（手机号、Email、账号等）。|
|password|string|是|密码。|
|type|int|是|认证类型：0-自有账号。|

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
    "inviter": "邀请人",
    "inviteCount": "邀请人数",
    "code": "唯一编码",
    "register": "注册时间",
    "grade": "等级：<50为用户；>=50为管理员；99为超级管理员",
    "state": "状态：0-正常；1-禁用"
}
```
