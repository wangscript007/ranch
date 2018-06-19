# 保存

请求
- Service Key - ranch.editor.element.save
- URI - /editor/element/save

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|char(36)|否|ID值：为空或不存在则新建；否则修改。|
|editor|char(36)|是|编辑器。|
|parent|char(36)|否|父元素，为空表示根节点。|
|sort|int|否|顺序，小于等于0表示不修改。|
|text|string|否|文本，用于全文检索。|
|modify|long|是|修改时间，新元素可为空。|

返回值
```json
{
  "editor": "编辑器",
  "parent": "父元素",
  "sort": "顺序",
  "text": "文本",
  "create": "创建时间",
  "modify": "修改时间",
}
```