# 检索全局公告

请求
- Service Key - ranch.notice.global
- URI - /notice/global

参数

|名称|类型|必须|说明|
|---|---|---|---|
|type|string|否|类型，为空则表示全部。|

返回值
```json
[{
    "user": "用户",
    "type": "类型",
    "subject": "标题",
    "content": "内容",
    "link": "链接",
    "read": "已读：0-否；1-是",
    "time": "时间"
}]
```
