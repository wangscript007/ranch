# 保存

请求
- Service Key - ranch.ad.save
- URI - /ad/save

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|char(36)|否|ID值，不存在则新增，存在则修改。|
|type|char(100)|是|类型。|
|sort|int|否|顺序。|
|name|char(100)|否|名称。|
|resource|char(100)|是|资源地址。|
|operation|char(100)|否|操作。|
|target|string|否|目标地址。|
|state|int|否|状态：0-下线；1-上线。|

返回值
```
""
```

## 上传

上传文件可使用以下任一接口：
- [上传文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/upload.md)
- [上传多文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/uploads.md)
- [HTTP上传文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl-http/doc/upload.md)

上传时需将`name`或`key`设置为`ranch.ad`。


> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
