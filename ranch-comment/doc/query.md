# 检索所有评论

请求
- Service Key - ranch.comment.query
- URI - /comment/query

参数
- audit 审核状态：0-待审核；1-审核通过；2-审核不通过。
- owner 所有者ID值。
- author 作者ID值。
- start 开始时间，格式yyyy-MM-dd。
- end 结束时间，格式yyyy-MM-dd。
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
            "owner": {},
            "author": {},
            "subject": "标题",
            "label": "标签",
            "content": "内容",
            "children": []
        }
    ]
}
```
- owner 所有者信息，未找到则仅包含id属性。
- author 作者信息，未找到则仅包含id属性。
- children 子评论集，如果不包含子评论则不返回children属性。

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
