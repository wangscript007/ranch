# 获取文档信息集

请求
- Service Key - ranch.doc.get
- URI - /doc/get

参数
- ids ID集。

返回值
```json
{
    "id-value": {
        "id": "ID值",
        "key": "类型KEY",
        "owner": {},
        "author": {},
        "scoreMin": "最小分值",
        "scoreMax": "最大分值",
        "sort": "顺序",
        "subject": "标题",
        "image": "主图URI地址",
        "thumbnail": "缩略图URI地址",
        "summary": "摘要",
        "label": "标签",
        "read": "阅读次数",
        "favorite": "收藏次数",
        "comment": "评论次数",
        "score": "得分",
        "time": "更新时间"
    }
}
```

- owner 所有者信息，未找到则仅包含id属性。
- author 作者信息，未找到则仅包含id属性。

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
