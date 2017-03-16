# 获取已验证通过的好友信息集

请求
- Service Key - ranch.friend.get
- URI - /friend/get

参数
- ids 好友信息ID集，多个ID以逗号分隔。

返回值
```json
{
    "id-value": {
        "owner": "所有者ID",
        "user": "用户ID",
        "memo": "备注",
        "state": "状态：0-待对方确认；1-待己方确认；2-已通过；3-已拒绝/拉黑",
        "create": "创建时间"
    }
}
```

- id-value 为好友信息的ID值。
