# 检索群组成员集

请求
- Service Key - ranch.group.member.query-by-group
- URI - /group/member/query-by-group

参数
- group 群组ID。

返回值
```json
[
    {
        "id": "成员ID",
        "user": {},
        "nick": "群组昵称",
        "type": "类型：0-待审核；1-普通成员；2-管理员；3-所有者",
        "join": "加入时间"
    }
]
```

- user为[用户](../../../ranch-user/)对象。
