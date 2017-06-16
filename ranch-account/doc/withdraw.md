# 取出

请求
- Service Key - ranch.account.withdraw
- URI - /account/withdraw

参数

|名称|类型|说明|
|---|---|---|
|user|char(36)|用户ID，为空则使用当前用户ID。|
|owner|char(100)|所有者，可选。|
|type|int|类型，0-9之间，默认：0。|
|amount|int|数量，大于0。|

> 其他扩展字段，将被保存到账户[变更日志](log/query.md)中。

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
    "consume": "消费总额",
    "logId": "账户日志ID"
}
```
