# 保存

请求
- Service Key - ranch.editor.element.save
- URI - /editor/element/save

参数

|名称|类型|说明|
|---|---|---|
|id|char(36)|ID值：为空或不存在则新建；否则修改。|
|editor|char(36)|编辑器。|
|parent|char(36)|父元素，为空表示根节点。|
|sort|int|顺序。|
|type|char(100)|类型。|
|x|int|X位置。|
|y|int|Y位置。|
|width|int|宽度。|
|height|int|高度。|
|modify|long|修改时间，新元素可为空。|

返回值
```json
{
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
}
```