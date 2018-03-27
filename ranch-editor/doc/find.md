# 查找

请求
- Service Key - ranch.editor.find
- URI - /editor/find

参数

|名称|类型|说明|
|---|---|---|
|id|char(36)|ID值。|
|user|char(36)|用户ID，为空则使用当前用户。|

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
  "modify": "修改时间",
  "role": "类型：0-所有者；1-可编辑；2-仅浏览"
}
```