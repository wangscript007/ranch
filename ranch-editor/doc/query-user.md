# 检索当前用户编辑器信息集

请求
- Service Key - ranch.editor.query-user
- URI - /editor/query-user

参数

|名称|类型|必须|说明|
|---|---|---|---|
|pageSize|int|否|每页显示记录数，默认20。|
|pageNum|int|否|当前显示页数。|

返回值
```json
{
    "count": "总记录数",
    "size": "每页显示记录数",
    "number": "当前显示页数",
    "list": [
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
            "source": "来源",
            "create": "创建时间",
            "modify": "修改时间"
        }
    ]
}
```