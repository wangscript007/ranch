# 取出

请求
- Service Key - ranch.account.withdraw
- URI - /account/withdraw

参数
- owner 所有者。
- type 类型。
- amount 数量。

返回值
```json
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
```
