# 账户

账户模块主要负责用户账户信息的维护，可以是现金、积分等类型的记账式数据。同一个用户可以拥有多个账户信息。

属性

|属性|类型|说明|
|---|---|---|
|user|char(36)|用户ID，必须。|
|owner|char(100)|所有者，可选。|
|type|int|类型，0-9之间。|
|balance|int|余额。|
|deposit|int|存入总额。|
|withdraw|int|取出总额。|
|reward|int|奖励总额。|
|profit|int|盈利总额。|
|consume|int|消费总额。|
|remitIn|int|汇入总额。|
|remitOut|int|汇出总额。|
|pending|int|待结算总额。|
|checksum|char(32)|校验值，上述属性的加盐MD5值。|

[检索账户集](doc/query.md)

[检索用户账户集](doc/query-user.md)

[获取合并账户信息](doc/merge.md)

[存入](doc/deposit.md)

[取出](doc/withdraw.md)

[奖励](doc/reward.md)

[盈利](doc/profit.md)

[消费](doc/consume.md)

[汇入](doc/remit-in.md)

[汇出](doc/remit-out.md)

## 变更日志

变更日志主要保存账户变动的日志详情，同时对变更进行审核，审核通过后账户才会变更。

属性

|属性|类型|说明|
|---|---|---|
|user|char(36)|用户ID，必须。|
|account|(36)|账户ID，必须。|
|type|char(100)|类型，必须。|
|amount|int|数量。|
|balance|int|余额。|
|state|int|状态：0-待处理；1-审核通过；2-审核不通过。|
|start|timestamp|开始时间，变更开始时间。|
|end|timestamp|结束时间，处理结束时间。|
|json|string|扩展属性集。|
|index|long|序号，自增，用于排序。|

[检索日志集](doc/log/query.md)

[审核通过](doc/log/pass.md)

[审核不通过](doc/log/reject.md)
