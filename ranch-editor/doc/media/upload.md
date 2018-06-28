# 上传

上传文件可使用以下任一接口：
- [上传文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/upload.md)
- [上传多文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/uploads.md)
- [HTTP上传文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl-http/doc/upload.md)

上传时需将`name`或`key`设置为`ranch.editor.media`，并添加以下参数：

|名称|类型|说明|
|---|---|---|
|editor|char(36)|编辑器ID值。|
|type|int|类型：0-背景；1-图片；2-音频；3-视频。|
|width|int|图片宽。|
|height|int|图片高。|
