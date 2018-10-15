# 保存

请求
- Service Key - ranch.editor.save
- URI - /editor/save

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|char(36)|否|ID值：为空或不存在则新建；否则修改。|
|type|char(100)|是|类型。|
|name|char(100)|是|名称。|
|label|char(100)|是|标签。|
|width|int|是|宽度。|
|height|int|是|高度。|
|image|char(100)|否|预览图。|

返回值
```json
{
  "template": "模板：0-否；1-是",
  "type": "类型",
  "sort": "顺序",
  "name": "名称",
  "label": "标签",
  "width": "宽度",
  "height": "高度",
  "image": "预览图",
  "screenshot": "主截图",
  "state": "状态：0-待审核；1-审核通过；2-审核拒绝；3-已上架；4-已下架；5-已删除",
  "source": "来源",
  "used": "被使用次数",
  "create": "创建时间",
  "modify": "修改时间",
  "role": "类型：0-所有者；1-可编辑；2-仅浏览"
}
```