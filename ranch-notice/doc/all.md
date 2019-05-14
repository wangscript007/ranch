# 检索发送所有人

请求
- Service Key - ranch.notice.all
- URI - /notice/all

参数

|名称|类型|必须|说明|
|---|---|---|---|
|type|string|否|类型，为空则表示全部。|
|subject|string|否|标题，模糊匹配。|
|time|string|否|发送时间范围，以逗号分隔，格式：`yyyy-MM-dd`或`yyyy-MM-dd HH:mm:ss`。|
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
            "link": "链接",
            "read": "已读：0-否；1-是",
            "time": "时间"
        }
    ]
}
```
