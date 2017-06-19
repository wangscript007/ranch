# 保存分类

如果code+key已存在则修改，不存在则新增。

请求
- Service Key - ranch.classify.save
- URI - /classify/save

参数

|名称|类型|说明|
|---|---|---|
|code|char(100)|编码，必须。|
|key|char(100)|键，必须。|
|value|char(100)|值。|
|name|char(100)|名称，必须。|

返回值
```json
{
    "id": "ID值",
    "code": "编码",
    "key": "键",
    "value": "值",
    "name": "名称"
}
```

> [扩展属性](json.md)

> [刷新缓存](refresh.md)

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
