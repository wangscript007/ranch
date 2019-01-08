# App Store

属性

|属性|类型|说明|
|---|---|---|
|productId|char(100)|产品ID。|
|name|char(100)|名称。|
|amount|int|金额，单位：分。|

[检索](doc/query.md)

[保存](doc/save.md)

[删除](doc/delete.md)

## 收据

属性

|属性|类型|说明|
|---|---|---|
|productId|char(100)|产品ID。|
|price|int|价格，单位：分。|
|quantity|int|数量。|
|amount|int|金额，单位：分。|
|status|int|状态。|
|md5|char(32)|请求MD5。|
|request|string|请求数据。|
|response|string|返回数据。|
|time|datetime|时间。|

[检索](doc/receipt/query.md)

[校验](doc/receipt/verify.md)