# 导出PDF

请求
- Service Key - ranch.editor.pdf
- URI - /editor/pdf

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|char(36)|是|ID值。|
|email|string|否|Email地址，如果为合法Email地址将在导出完成后将文件下载URL发送到此Email地址；需要配置[推送](../../ranch-push/)`key=ranch.editor.pdf`，并设置`data.url`参数为下载地址。|

返回值
```
"异步ID"
```

> 异步ID参考[异步服务](../../ranch-base/doc/async.md)。
