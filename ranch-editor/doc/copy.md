# 复制

请求
- Service Key - ranch.editor.copy
- URI - /editor/copy

参数

|名称|类型|说明|
|---|---|---|
|id|char(36)|源ID值。|
|type|char(100)|目标类型。|

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