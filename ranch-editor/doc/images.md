# 导出图片集

请求
- Service Key - ranch.editor.images
- URI - /editor/images

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|char(36)|是|ID值。|
|password|char(100)|否|密码。|
|email|string|否|Email地址，如果为合法Email地址将在导出完成后将文件下载URL发送到此Email地址；需要配置[推送](../../ranch-push/)`key=ranch.editor.images`，并设置`data.url`参数为下载地址，下载地址`3`天内有效。|

返回值
```
"异步ID"
```

> 异步ID参考[异步服务](../../ranch-base/doc/async.md)。
