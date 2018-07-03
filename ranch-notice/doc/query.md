# 检索

请求
- Service Key - ranch.notice.query
- URI - /notice/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|type|string|否|类型，为空则表示全部。|
|read|int|否|已读：-1-全部；0-否；1-是。|
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
            "type": "类型",
            "subject": "标题",
            "content": "内容",
            "read": "已读：0-否；1-是",
            "time": "时间"
        }
    ]
}
```
