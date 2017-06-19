# 获取分类信息集

请求
- Service Key - ranch.classify.get
- URI - /classify/get

参数

|名称|类型|说明|
|---|---|---|
|ids|string|分类信息ID集，多个ID以逗号分隔。|

返回值
```json
{
    "id-value": {
        "id": "ID值",
        "code": "编码",
        "key": "键",
        "value": "值",
        "name": "名称"
    }
}
```

- id-value 为分类信息的ID值。

> [扩展属性](json.md)

> [刷新缓存](refresh.md)
