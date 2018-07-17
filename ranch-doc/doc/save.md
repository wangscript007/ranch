# 保存文档信息集

请求
- Service Key - ranch.doc.save
- URI - /doc/save

参数

|属性|类型|说明|
|---|---|---|
|id|char(36)|ID值，如果不存在则新增。|
|key|char(100)|是|类型KEY。|
|sort|int|否|顺序。|
|subject|char(100)|是|标题。|
|image|char(100)|否|主图URI地址。|
|thumbnail|char(100)|否|缩略图URI地址。|
|summary|string|否|摘要。|
|label|string|否|标签。|
|source|string|是|内容源。|
|markdown|boolean|否|是否为Markdown文档：true-是；其他-否。|

上传图片

上传文件可使用以下任一接口：
- [上传文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/upload.md)
- [上传多文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/uploads.md)
- [HTTP上传文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl-http/doc/upload.md)

上传时需将`name`或`key`设置为`ranch.doc.image`。

返回值
```json
{
    "id": "ID值",
    "key": "类型KEY",
    "author": {},
    "sort": "顺序",
    "subject": "标题",
    "image": "主图URI地址",
    "thumbnail": "缩略图URI地址",
    "summary": "摘要",
    "label": "标签",
    "read": "阅读次数",
    "favorite": "收藏次数",
    "comment": "评论次数",
    "praise": "点赞数",
    "score": "得分",
    "time": "更新时间"
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
