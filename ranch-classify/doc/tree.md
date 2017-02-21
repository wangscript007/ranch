# 检索分类信息树

请求
- Service Key - ranch.classify.tree
- URI - /classify/tree

参数
- code 编码前缀，会自动匹配【code+%】。

返回值
```json
[
    {
        "id": "ID值",
        "code": "编码",
        "key": "键",
        "value": "值",
        "name": "名称",
        "children": []
    }
]
```

- children 子分类集，如果不包含子分类则不返回children属性。

> [扩展属性](json.md)

> [刷新缓存](refresh.md)
