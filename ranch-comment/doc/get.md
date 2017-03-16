# 获取评论信息集

请求
- Service Key - ranch.comment.get
- URI - /comment/get

参数
- ids 评论信息ID集，多个ID以逗号分隔。

返回值
```json
{
    "id-value": {
        "id": "ID值",
        "owner": {},
        "author": {},
        "subject": "标题",
        "label": "标签",
        "content": "内容",
        "children": []
    }
}
```

- owner 所有者信息，未找到则仅包含id属性。
- author 作者信息，未找到则仅包含id属性。
- children 子评论集，如果不包含子评论则不返回children属性。

> [扩展属性](json.md)

> [刷新缓存](refresh.md)
