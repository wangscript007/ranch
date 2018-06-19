# 检索

请求
- Service Key - ranch.editor.elemet.query
- URI - /editor/element/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|editor|char(36)|是|编辑器ID值。|
|parent|char(36)|否|父元素，为空则表示根元素。|
|user|char(36)|否|用户ID，为空则使用当前用户。|
|recursive|boolean|否|是否递归子元素：true-递归；其它-不递归。|

返回值
```json
[{
  "editor": "编辑器",
  "parent": "父元素",
  "sort": "顺序",
  "text": "文本",
  "create": "创建时间",
  "modify": "修改时间",
  "children": []
}]
```

> 仅当`recursive=true`时返回`children`属性。