# 创建

请求
- Service Key - ranch.editor.speech.create
- URI - /editor/speech/create

参数

|名称|类型|必须|说明|
|---|---|---|---|
|editor|char(36)|是|编辑器ID。|

返回值
```json
{
    "user": "用户",
    "editor": "编辑器",
    "name": "名称",
    "width": "宽度",
    "height": "高度",
    "image": "预览图",
    "password": "密码",
    "wsUrl": "WebSocket地址",
    "state": "状态：0-未开始；1-演示中；2-已结束",
    "time": "时间"
}
```