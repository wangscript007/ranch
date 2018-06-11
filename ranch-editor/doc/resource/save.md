# 保存

请求
- Service Key - ranch.editor.element.save
- URI - /editor/element/save

参数

|名称|类型|说明|
|---|---|---|
|id|char(36)|ID值：为空或不存在则新建；否则修改。|
|type|char(100)|分类。|
|sort|int|顺序。|
|name|char(100)|名称。|
|label|char(100)|说明。|
|uri|char(100)|资源URI地址。|
|width|int|图片宽。|
|height|int|图片高。|
|thumbnail|char(100)|缩略图URI地址。|

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