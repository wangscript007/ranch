# 判断关联数据是否存在

请求
- Service Key - ranch.link.exists
- URI - /link/exists

参数

|名称|类型|说明|
|---|---|---|
|type|char(100)|类型，必须。|
|id1s|string|关联ID1集，以逗号分隔。|
|id2s|string|关联ID2集，以逗号分隔。|

返回值
```
[{
    "id1": "关联ID1",
    "id2": "关联ID2",
    "exists": "true/false"
}]
```
