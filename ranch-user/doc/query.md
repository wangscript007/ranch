# 检索用户

请求
- Service Key - ranch.user.query
- URI - /user/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|uid|char(100)|否|UID，为空则检索全部。|
|idcard|char(100)|否|身份证号，为空则检索全部。|
|name|char(100)|否|姓名，为空则检索全部。|
|nick|char(100)|否|昵称，为空则检索全部。|
|mobile|char(100)|否|手机号，为空则检索全部。|
|email|char(100)|否|Email地址，为空则检索全部。|
|code|char(36)|否|唯一编码，为空则检索全部。|
|minGrade|int|否|最小等级，为空则检索全部。|
|maxGrade|int|否|最大等级，为空则检索全部。|
|state|int|否|状态：-1：全部；0-正常；1-禁用。|
|register|string|否|注册日期范围，以逗号分隔，格式：yyyy-MM-dd，为空则检索全部。|
|pageSize|int|否|每页显示记录数，默认20。|
|pageNum|int|否|当前显示页数。|

返回值
```json
{
    "count": "总记录数",
    "size": "每页显示记录数",
    "number": "当前显示页数",
    "list": [
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
    ]
}
```

> 返回列表按注册时间降序排列。

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
