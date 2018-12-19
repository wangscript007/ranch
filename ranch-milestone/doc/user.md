# 当前用户里程碑集

请求
- Service Key - ranch.milestone.user
- URI - /milestone/user

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
            "user": "用户",
            "type": "类型",
            "json": "自定义属性集",
            "Timestamp time": "时间"
        }
    ]
}
```