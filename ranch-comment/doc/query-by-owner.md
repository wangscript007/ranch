# 检索所有者评论

参数
- owner 所有者ID值。
- pageSize 每页显示记录数。
- pageNum 当前显示页数。

返回值
```json
{
  "count":"总记录数",
  "size":"每页显示记录数",
  "number":"当前显示页数",
  "list":[{
    "id": "ID值。",
    "author": {},
    "author-comment": "作者信息，未找到则仅包含id属性。",
    "subject": "标题",
    "label": "标签",
    "content": "内容",
    "children": [],
    "children-comment": "如果不包含子评论则不返回children属性。"
  }]
}
```
