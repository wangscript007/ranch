# 保存配置

请求
- Service Key - ranch.weixin.save
- URI - /weixin/save

参数

|名称|类型|说明|
|---|---|---|
|key|char(100)|引用key，不可重复。|
|name|char(100)|名称。|
|appId|char(100)|APP ID，不可重复。|
|secret|char(100)|密钥。|
|token|char(100)|验证Token。|
|mchId|char(100)|商户ID。|
|mchKey|char(100)|商户密钥。|
|menu|string|菜单配置。|

返回值
```json
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
  "menu": "菜单配置",
  "time": "更新时间"
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)，签名密钥名为`ranch-weixin`。
