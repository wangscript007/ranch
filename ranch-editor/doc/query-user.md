# 检索当前用户编辑器信息集

请求
- Service Key - ranch.editor.query-user
- URI - /editor/query-user

参数

|名称|类型|说明|
|---|---|---|
|pageSize|int|每页显示记录数，默认20。|
|pageNum|int|当前显示页数。|

返回值
```json
{
    "count": "总记录数",
    "size": "每页显示记录数",
    "number": "当前显示页数",
    "list": [
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
    ]
}
```