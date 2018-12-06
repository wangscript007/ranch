# 用户数据集

请求
- Service Key - ranch.editor.speech.user
- URI - /editor/speech/user

参数

|名称|类型|必须|说明|
|---|---|---|---|
|time|string|否|时间范围，以逗号分隔，支持：yyyy-MM-dd及yyyy-MM-dd HH:mm:ss格式。|
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
            "user": "用户",
            "editor": "编辑器",
            "name": "名称",
            "width": "宽度",
            "height": "高度",
            "image": "预览图",
            "password": "密码",
            "wsUrl": "WebSocket地址",
            "time": "时间"
        }
    ]
}
```