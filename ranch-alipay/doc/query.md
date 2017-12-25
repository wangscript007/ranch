# 检索配置集

请求
- Service Key - ranch.alipay.query
- URI - /alipay/query

参数

无

返回值
```json
[
  {
    "id": "ID值",
    "key": "引用key",
    "name": "名称",
    "appId": "APP ID",
    "privateKey": "私钥",
    "publicKey": "公钥"
  }
]
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)，签名密钥名为`ranch-alipay`。
