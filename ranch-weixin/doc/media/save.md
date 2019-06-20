# 新增

请求
- Service Key - ranch.weixin.media.create
- URI - /weixin/media/create

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|char(100)|是|微信key。|
|type|char(100)|是|类型：image-图片；voice-语音；video-视频；thumb-缩略图。|
|name|char(100)|否|名称。|
|uri|char(100)|是|文件URI。|

返回值
```
""
```

## 上传文件

上传文件可使用以下任一接口：
- [上传文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/upload.md)
- [上传多文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/uploads.md)
- [HTTP上传文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl-http/doc/upload.md)

上传时需将`name`或`key`设置为`ranch.weixin.media`，目前只支持上传`图片`。

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)，签名密钥名为`ranch-weixin`。
