# 发布

请求
- Service Key - ranch.ad.publish
- URI - /ad/publish

参数

|名称|类型|必须|说明|
|---|---|---|---|
|type|string|否|类型，空表示全部，多个以逗号分隔。|

返回值
```json
[{
    "type": "类型",
    "sort": "顺序",
    "name": "名称",
    "resource": "资源地址",
    "operation": "操作",
    "target": "目标地址",
    "state": "状态：0-下线；1-上线"
}]
```
