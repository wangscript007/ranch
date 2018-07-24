# 检索已发布文档信息集

请求
- Service Key - ranch.doc.index
- URI - /doc/index

参数

|名称|类型|必须|说明|
|---|---|---|---|
|classify|char(36)|是|分类ID。|
|pageSize|int|否|每页显示记录数，默认20。|
|pageNum|int|否|当前显示页数。|

返回值
```json
{
    "count": "总记录数",
    "size": "每页显示记录数",
    "number": "当前显示页数",
    "list": [
        {
            "id": "ID值",
            "classifies": ["分类集"],
            "author": {},
            "sort": "顺序",
            "subject": "标题",
            "image": "主图URI地址",
            "thumbnail": "缩略图URI地址",
            "summary": "摘要",
            "label": "标签",
            "type": "类型",
            "read": "阅读次数",
            "favorite": "收藏次数",
            "comment": "评论次数",
            "praise": "点赞数",
            "score": "得分",
            "time": "更新时间"
        }
    ]
}
```
