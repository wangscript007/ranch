# 保存

请求
- Service Key - ranch.resource.save
- URI - /resource/save

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|char(36)|否|ID值：为空或不存在则新建；否则修改。|
|type|char(100)|是|分类。|
|sort|int|否|顺序。|
|name|char(100)|否|名称。|
|label|char(100)|否|说明。|
|uri|char(100)|否|资源URI地址。|
|size|int|否|文件大小。|
|width|int|否|图片宽。|
|height|int|否|图片高。|
|thumbnail|char(100)|否|缩略图URI地址。|
|author|char(100)|否|作者。|
|download|string|否|资源下载URL地址。|

> 如果`download`不为空，则远程下载该资源，此时`uri`参数无效。

返回值
```json
{
    "type": "类型",
    "sort": "顺序",
    "name": "名称",
    "label": "说明",
    "uri": "资源URI地址",
    "size": "文件大小",
    "width": "图片宽",
    "height": "图片高",
    "thumbnail": "缩略图URI地址",
    "author": "作者",
    "state": "状态：0-待审核；1-审核通过；2-审核拒绝；3-已上架；4-已下架",
    "user": "用户",
    "time": "时间"
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。