# 删除

请求
- Service Key - ranch.weixin.media.delete
- URI - /weixin/media/delete

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|char(36)|是|ID值。|

返回值
```
""
```

> 删除时会同步删除微信上的素材。

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)，签名密钥名为`ranch-weixin`。
