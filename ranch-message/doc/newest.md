# 获取最新消息集

请求
- Service Key - ranch.message.newest
- URI - /message/newest

参数
- time 上一次获取时间戳整数值，精确到毫秒，如果未提供或小于等于0则使用系统记录的上一次请求时间。

返回值
```json
{
    "time": "服务器时间戳（单位：毫秒），下次请求时提供",
    "messages": [
        {
            "id": "好友ID或群组ID",
            "group": "是否为群组消息，true/false",
            "list": [
                {
                    "id": "消息ID",
                    "sender": "发送者ID",
                    "type": "接收者类型：0-好友；1-群组",
                    "receiver": "接收者ID",
                    "format": "消息格式：0-文本；1-图片；2-音频；3-视频；4-文件；5-红包；6-转账；7-名片；8-公告；9-通知",
                    "content": "内容",
                    "time": "发送时间",
                    "deadline": "失效时间"
                }
            ]
        }
    ]
}
```
