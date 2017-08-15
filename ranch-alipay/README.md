## 支付宝

支付宝模块主要实现支付宝接口适配。

属性

|属性|类型|说明|
|---|---|---|
|key|char(100)|引用key，不可重复。|
|name|char(100)|名称。|
|url|char(100)|支付宝接口URL地址。|
|appId|char(100)|APP ID，不可重复。|
|privateKey|string|私钥。|
|publicKey|string|公钥。|

[检索配置集](doc/query.md)

[保存配置](doc/save.md)

[发起手机WEB端支付](doc/quick-wap-pay.md)

[发起PC WEB端支付](doc/fast-instant-trade-pay.md)

[发起APP端支付](doc/quick-msecurity-pay.md)
