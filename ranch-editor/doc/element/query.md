# 检索

请求
- Service Key - ranch.editor.elemet.query
- URI - /editor/element/query

参数

|名称|类型|说明|
|---|---|---|
|editor|char(36)|ID值。|
|parent|char(36)|父元素，为空则表示根元素。|
|user|char(36)|用户ID，为空则使用当前用户。|
|recursive|boolean|是否递归子元素：true-递归；其它-不递归。|

返回值
```json
[{
  "editor": "编辑器",
  "parent": "父元素",
  "sort": "顺序",
  "type": "类型",
  "x": "X位置",
  "y": "Y位置",
  "width": "宽度",
  "height": "高度",
  "create": "创建时间",
  "modify": "修改时间",
  "children": []
}]
```
> 仅当`recursive=true`时返回`children`属性。