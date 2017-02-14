# 创建新评论

请求
- Service Key - ranch.comment.create
- URI - /comment/create

参数
- key 服务key，用于标记服务类型。
- owner 所有者ID，如文章、商品ID等。
- author 作者ID。
- subject 标题。
- label 标签。
- content 内容。
- score 评分[0-5]。

返回值
```json
{
  "id": "ID值",
  "owner": {},
  "owner-comment": "所有者信息，未找到则仅包含id属性",
  "author": {},
  "author-comment": "作者信息，未找到则仅包含id属性",
  "subject": "标题",
  "label": "标签",
  "content": "内容",
  "children": [],
  "children-comment": "如果不包含子评论则不返回children属性"
}
```
