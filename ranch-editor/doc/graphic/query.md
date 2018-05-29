# 检索

请求
- Service Key - ranch.editor.graphic.query
- URI - /editor/graphic/query

参数

|名称|类型|说明|
|---|---|---|
|type|char(100)|分类，为空则表示全部。|
|name|char(100)|名称，为空则表示全部。|
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
            "label": "说明",
            "svg": "SVG图"
        }
    ]
}
```