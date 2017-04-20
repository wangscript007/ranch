# 检索账户集

请求
- Service Key - ranch.account.query
- URI - /account/query

参数
- user 用户ID，如果为空则为当前用户。
- owner 所有者，如果未提供则表示所有。

返回值
```json
[
    {
        "id": "ID值",
        "user": "用户",
        "owner": "所有者",
        "type": "类型",
        "balance": "余额",
        "deposit": "存入总额",
        "withdraw": "取出总额",
        "reward": "奖励总额",
        "profit": "盈利总额",
        "consume": "消费总额"
    }
]
```
