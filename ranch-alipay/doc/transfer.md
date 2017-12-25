# 转账

请求
- Service Key - ranch.alipay.transfer
- URI - /alipay/transfer

参数

|名称|类型|说明|
|---|---|---|
|key|char(100)|引用key，必须。|
|user|char(36)|用户ID，为空则使用当前用户ID。|
|account|char(100)|收款方支付宝账户，Email或手机号，必须。|
|amount|int|金额，单位：分。|
|billNo|char(100)|单据号。|
|realName|string|真实姓名，不为空时校验姓名与账号是否匹配，空时不校验。|
|showName|string|显示名称，为空显示付款方的支付宝认证姓名或单位名称。|
|notice|string|[通知配置](notice.md)。|

返回值
```text
true/false
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)，签名密钥名为`ranch-alipay`。
