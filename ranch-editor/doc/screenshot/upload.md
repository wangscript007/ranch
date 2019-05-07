# 上传

上传文件可使用以下任一接口：
- [上传文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/upload.md)
- [上传多文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/uploads.md)
- [HTTP上传文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl-http/doc/upload.md)

上传时需将`name`或`key`设置为`ranch.editor.screenshot`，并添加以下参数：

|名称|类型|必须|说明|
|---|---|---|---|
|id|char(36)|是|ID值。|

> 当前用户等级必须大于等于`50`。
