# 检索分类信息集

请求
- Service Key - ranch.classify.list
- URI - /classify/list

参数
- key 关键词前缀，会自动匹配【key+%】。
- pageSize 显示数据数，如果未设置则使用${ranch.classify.list.size}设置值。

返回值
```json
[
    {
        "id": "ID值",
        "code": "编码",
        "key": "关键词",
        "name": "名称"
    }
]
```

> [扩展属性](json.md)

> [刷新缓存](refresh.md)
