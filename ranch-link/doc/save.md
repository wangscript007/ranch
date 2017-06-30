# 保存

请求
- Service Key - ranch.link.save
- URI - /link/save

参数

|名称|类型|说明|
|---|---|---|
|type|char(100)|类型，必须。|
|id1|char(36)|关联ID1，必须。|
|id2|char(36)|关联ID2，必须。|
> 其TA自定义属性直接添加到参数中。

返回值
```json
{
  "type": "类型",
  "id1": "关联ID1",
  "id2": "关联ID2",
  "time": "更新时间"
}
```
> 包含自定义属性。
