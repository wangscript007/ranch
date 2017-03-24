# 检索类型KEY的文档信息集

请求
- Service Key - ranch.doc.query-by-key
- URI - /doc/query-by-key

参数
- audit 审核状态：0-待审核；1-审核通过；2-审核不通过。
- key 类型KEY。
- pageSize 每页显示记录数。
- pageNum 当前显示页数。

返回值
```json
{
    "count": "总记录数",
    "size": "每页显示记录数",
    "number": "当前显示页数",
    "list": [
        {
            "id": "ID值",
            "key": "类型KEY",
            "owner": "所有者ID",
            "author": "作者ID",
            "scoreMin": "最小分值",
            "scoreMax": "最大分值",
            "sort": "顺序",
            "subject": "标题",
            "image": "主图URI地址",
            "thumbnail": "缩略图URI地址",
            "summary": "摘要",
            "label": "标签",
            "source": "内容源",
            "content": "内容",
            "read": "阅读次数",
            "favorite": "收藏次数",
            "comment": "评论次数",
            "score": "得分",
            "time": "更新时间"
        }
    ]
}
```

- owner 所有者信息，未找到则仅包含id属性。
- author 作者信息，未找到则仅包含id属性。

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
