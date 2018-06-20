# 查找

请求
- Service Key - ranch.editor.find
- URI - /editor/find

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|char(36)|是|ID值。|
|user|char(36)|否|用户ID，为空则使用当前用户。|

返回值
```json
{
  "template": "模板：0-否；1-是",
  "type": "类型",
  "sort": "顺序",
  "name": "名称",
  "keyword": "关键词",
  "width": "宽度",
  "height": "高度",
  "image": "预览图",
  "screenshot": "主截图",
  "state": "状态：0-待审核；1-审核通过；2-审核拒绝；3-已上架；4-已下架",
  "create": "创建时间",
  "modify": "修改时间",
  "role": "类型：0-所有者；1-可编辑；2-仅浏览"
}
```