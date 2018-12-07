# 获取详情

请求
- Service Key - ranch.editor.speech.info
- URI - /editor/speech/info

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|char(36)|是|ID值。|

返回值
```json
{
    "user": "用户",
    "editor": "编辑器",
    "name": "名称",
    "width": "宽度",
    "height": "高度",
    "image": "预览图",
    "password": "true/false",
    "wsUrl": "WebSocket地址",
    "state": "状态：0-未开始；1-演示中；2-已结束",
    "personal": "私有：0-否；1-是",
    "time": "时间"
}
```