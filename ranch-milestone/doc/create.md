# 新增

请求
- Service Key - ranch.milestone.create
- URI - /milestone/create

参数

|名称|类型|必须|说明|
|---|---|---|---|
|user|char(36)|是|用户。|
|type|char(100)|是|类型。|

> 支持扩展属性。

返回值
```json
{
    "user": "用户",
    "type": "类型",
    "json": "自定义属性集",
    "Timestamp time": "时间"
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
