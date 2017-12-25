# 检索配置集

请求
- Service Key - ranch.weixin.query
- URI - /weixin/query

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
    "secret": "密钥",
    "token": "验证Token",
    "mchId": "商户ID",
    "mchKey": "商户密钥",
    "accessToken": "当前Access Token",
    "jsapiTicket": "当前Jsapi Ticket",
    "time": "更新时间"
  }
]
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)，签名密钥名为`ranch-weixin`。
