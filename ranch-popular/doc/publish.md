# 发布

请求
- Service Key - ranch.popular.publish
- URI - /popular/publish

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|char(100)|是|引用key。|
|size|int|否|数量，默认10。|

返回值
```json
[{
    "id": "ID值",
    "key": "引用key",
    "value": "值",
    "count": "次数",
    "state": "状态：0-正常；1-禁用"
}]
```
