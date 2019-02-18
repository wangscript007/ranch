# PayPal

属性

|属性|类型|说明|
|---|---|---|
|key|char(100)|引用key。|
|name|char(100)|名称。|
|appId|char(100)|APP ID。|
|secret|string|密钥。|

[检索](doc/query.md)

[保存](doc/save.md)

[删除](doc/delete.md)

## 交易

属性

|属性|类型|说明|
|---|---|---|
|key|char(100)|引用key。|
|user|char(36)|用户。|
|orderNo|char(100)|订单号。|
|tradeNo|char(100)|交易单号。|
|amount|int|金额，单位：分。|
|currency|char(100)|币种。|
|response|string|验证结果。|
|create|datetime|创建时间。|
|finish|datetime|完成时间。|

[检索](doc/transaction/query.md)

[校验](doc/transaction/verify.md)