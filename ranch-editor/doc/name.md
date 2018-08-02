# 修改名称

请求
- Service Key - ranch.editor.name
- URI - /editor/name

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|char(36)|是|ID值。|
|name|char(100)|是|名称。|

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
  "state": "状态：0-待审核；1-审核通过；2-审核拒绝；3-已上架；4-已下架",
  "source": "来源",
  "used": "被使用次数",
  "create": "创建时间",
  "modify": "修改时间",
  "role": "类型：0-所有者；1-可编辑；2-仅浏览"
}
```