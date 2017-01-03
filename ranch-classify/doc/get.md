# 获取分类信息

请求：
- Service Key - ranch.classify.get
- URI - /classify/get

参数
- ids 分类信息ID集，多个ID以逗号分隔。
- links 是否解析链接映射。

返回值
```json
{
    "id-value": {
        "id": "ID值。",
        "code": "编码",
        "name": "名称",
        "label": "标签"
    }
}
```

- id-value 为分类信息的ID值。
- 如果links设置为true，且当label包含名为links的json数据，则将links数据一并返回。
