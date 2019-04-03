# 上传

上传文件可使用以下任一接口：
- [上传文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/upload.md)
- [上传多文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/uploads.md)
- [HTTP上传文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl-http/doc/upload.md)

上传时需将`name`或`key`设置为`ranch.editor.file`，并添加以下参数：

|名称|类型|必须|说明|
|---|---|---|---|
|editor|char(36)|否|编辑器ID值，新增时需要提供。|
|type|char(100)|否|类型：pdf/ppt。|
|etype|char(100)|否|编辑器类型。|

> 如果`editor+type`已存在则修改，否则新增。
