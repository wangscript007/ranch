# 检索文档信息集

请求
- Service Key - ranch.doc.query
- URI - /doc/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|classify|char(36)|否|分类。|
|author|char(36)|否|作者，ID或UID。|
|category|char(100)|否|类别。|
|subject|char(100)|否|标题，模糊匹配。|
|label|char(100)|否|标签，模糊匹配。|
|type|char(100)|否|类型。|
|audit|int|否|审核状态：-1-全部；0-待审核；1-审核通过；2-审核不通过。|
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
            "category": "类别",
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

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
