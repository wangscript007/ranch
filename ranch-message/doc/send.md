# 发送消息

请求
- Service Key - ranch.message.send
- URI - /message/send

参数
- type 接收者类型：0-好友；1-群组。
- receiver 接收者ID，好友ID、群组ID等。
- format 消息格式：0-文本；1-图片；2-音频；3-视频；4-文件；5-红包；6-转账；7-名片；8-公告；9-通知。
- content 内容。
- code 校验码，纯字母+数字，长度不超过64个字符，需全局唯一。

返回值
```text
"id"
```

> 校验码为客户端生成的全局唯一的编码（如UUID），用于校验消息是否重复发送。
