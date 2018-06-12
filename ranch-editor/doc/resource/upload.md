# 上传

上传文件可使用以下任一接口：
- [上传文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/upload.md)
- [上传多文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/uploads.md)
- [HTTP上传文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl-http/doc/upload.md)

上传时需将`name`或`key`设置为`ranch.editor.resource`，支持的资源类型如下：

|类型|后缀|
|---|---|
|image/jpeg|.jpg or .jpeg|
|image/png|.png|
|image/gif|.gif|
|image/svg+xml|.svg|
|audio/mpeg|.mp3|
|application/json|.json|

> 用户需要处于已登入状态。
