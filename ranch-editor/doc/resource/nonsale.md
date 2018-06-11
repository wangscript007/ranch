# 下架

请求
- Service Key - ranch.editor.resource.nonsale
- URI - /editor/resource/nonsale

参数

|名称|类型|说明|
|---|---|---|
|id|char(36)|ID值。|

返回值
```json
{
    "type": "类型",
    "sort": "顺序",
    "name": "名称",
    "label": "说明",
    "uri": "资源URI地址",
    "width": "图片宽",
    "height": "图片高",
    "thumbnail": "缩略图URI地址",
    "state": "状态：0-待审核；1-审核通过；2-审核拒绝；3-已上架；4-已下架",
    "user": "用户",
    "time": "时间"
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。