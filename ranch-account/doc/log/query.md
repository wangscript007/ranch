# 检索账户变更日志集

请求
- Service Key - ranch.account.log.query
- URI - /account/log/query

参数

|名称|类型|说明|
|---|---|---|
|uid|string|用户UID，如果为空则为所有用户。|
|start|string|开始日期，格式：yyyy-MM-dd，为空则不限制。|
|end|string|结束日期，格式：yyyy-MM-dd，为空则不限制。|
|pageSize|int|每页显示记录数，小于等于0则默认20。|
|pageNum|int|当前显示页数。|

返回值
```json
{
    "count": "总记录数",
    "size": "每页显示记录数",
    "number": "当前显示页数",
    "list": [
        {
            "user": {},
            "type": "类型",
            "amount": "数量",
            "balance": "余额",
            "state": "状态：0-待处理；1-审核通过；2-审核不通过；3-已完成",
            "start": "开始时间",
            "end": "结束时间"
        }
    ]
}
```

- user 用户信息，未找到则仅包含id属性。

> 包含其他扩展属性。

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
