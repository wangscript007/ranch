# 获取好友信息集

请求
- Service Key - ranch.friend.query
- URI - /friend/query

参数
- state 状态：0-待对方确认；1-待己方确认；2-已通过；3-已拒绝/拉黑。

返回值
```json
[
    {
        "id": "ID值",
        "owner": "所有者ID",
        "user": {},
        "memo": "备注",
        "state": "状态：0-待对方确认；1-待己方确认；2-已通过；3-已拒绝/拉黑",
        "create": "创建时间"
    }
]
```
- user 为好友信息，具体内容可参考[用户](../../ranch-user/)模块。
