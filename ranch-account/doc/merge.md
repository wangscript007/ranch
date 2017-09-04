# 获取合并账户信息

请求
- Service Key - ranch.account.merge
- URI - /account/merge

参数

|名称|类型|说明|
|---|---|---|
|user|char(36)|用户ID，为空则使用当前用户ID。|
|owner|char(100)|所有者，可选。|
|fill|boolean|是否填充用户信息，true or false。|

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
    "remitIn": "汇入总额",
    "remitOut": "汇出总额"
}
```

> 合并时按比例将`type>0`的值按配置比例合并到`type=0`的账户中。比例配置可通过`ranch.classify`模块设置：code=ranch.account,key=merge.rate.${type},value=1.0。未设置时默认为`1.0`。
