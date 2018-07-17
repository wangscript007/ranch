# 获取用户信息

请求
- Service Key - ranch.user.get
- URI - /user/get

参数

|名称|类型|必须|说明|
|---|---|---|---|
|ids|string|是|要获取的用户ID集，多个ID间以逗号分隔。|

返回值
```json
{
    "id-1": {
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
    },
    "id-n": {
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
}
```

> 内部服务接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
