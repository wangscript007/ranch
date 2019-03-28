# 下载

请求
- Service Key - ranch.editor.file.download
- URI - /editor/file/download

参数

|名称|类型|必须|说明|
|---|---|---|---|
|editor|char(36)|是|编辑器ID值。|
|type|char(100)|是|目标类型。|
|email|string|否|Email地址，如果为合法Email地址将在导出完成后将文件下载URL发送到此Email地址；需要配置[推送](../../ranch-push/)`key=ranch.editor.file.download`，并设置`data.url`参数为下载地址，下载地址`1`天内有效。|

返回值
```
文件下载URL地址
```
