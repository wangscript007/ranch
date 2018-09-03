# 修改用户信息

请求
- Service Key - ranch.user.update
- URI - /user/update

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|char(100)|是|ID值。|
|idcard|char(100)|否|身份证号。|
|name|char(100)|否|姓名。|
|nick|char(100)|否|昵称。|
|mobile|char(100)|否|手机号。|
|email|char(100)|否|Email地址。|
|portrait|char(100)|否|头像URI地址。|
|gender|int|否|性别：0-未知；1-男；2-女。|
|birthday|date|否|出生日期，格式：yyyy-MM-dd。|

上传头像可使用以下任一接口：

- [上传文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/upload.md)
- [上传多文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/uploads.md)
- [HTTP上传文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl-http/doc/upload.md)

上传时需将`name`或`key`设置为`ranch.user.portrait`。

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

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
