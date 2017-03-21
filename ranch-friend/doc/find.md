# 获取当前用户好友信息

请求
- Service Key - ranch.friend.find
- URI - /friend/find

参数
- user 用户信息。

返回值
```json
{
    "id": "ID值",
    "owner": "所有者ID",
    "user": "用户ID",
    "memo": "备注",
    "state": "状态：0-待对方确认；1-待己方确认；2-已通过；3-已拒绝/拉黑",
    "create": "创建时间"
}
```
