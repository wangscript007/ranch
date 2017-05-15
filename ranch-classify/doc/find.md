# 通过编码与key获取分类信息

请求
- Service Key - ranch.classify.find
- URI - /classify/find

参数
- code 编码，必须。
- key KEY值，必须。

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
