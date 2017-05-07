# 检索用户

请求
- Service Key - ranch.user.query
- URI - /user/query

参数
- mobile 手机号，为空则检索全部。
- pageSize 每页显示记录数。
- pageNum 当前显示页数。

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
