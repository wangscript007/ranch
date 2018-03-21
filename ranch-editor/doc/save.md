# 保存

请求
- Service Key - ranch.editor.save
- URI - /editor/save

参数

|名称|类型|说明|
|---|---|---|
|id|char(36)|ID值：为空或不存在则新建；否则修改。|
|type|char(100)|类型。|
|name|char(100)|名称。|
|keyword|char(100)|关键词。|
|width|int|宽度。|
|height|int|高度。|
|image|char(100)|预览图。|

返回值
```json
{
  "type": "类型",
  "sort": "顺序",
  "name": "名称",
  "keyword": "关键词",
  "width": "宽度",
  "height": "高度",
  "image": "预览图",
  "create": "创建时间",
  "modify": "修改时间"
}
```