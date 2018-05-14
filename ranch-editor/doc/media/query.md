# 检索

请求
- Service Key - ranch.editor.media.query
- URI - /editor/media/query

参数

|名称|类型|说明|
|---|---|---|
|editor|char(36)|编辑器ID值。|
|type|int|类型：0-背景；1-图片；2-音频；3-视频。|
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
            "user": "用户",
            "editor": "编辑器",
            "type": "类型：0-背景；1-图片；2-音频；3-视频",
            "url": "URL地址",
            "name": "文件名",
            "time": "时间",
        }
    ]
}
```