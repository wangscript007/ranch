# 检索账户集

请求
- Service Key - ranch.account.query
- URI - /account/query

参数

|名称|类型|说明|
|---|---|---|
|user|char(36)|用户ID，为空则使用当前用户ID。|
|owner|char(100)|所有者，可选。|

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
        "consume": "消费总额",
        "remitIn": "汇入总额",
        "remitOut": "汇出总额"
    }
]
```