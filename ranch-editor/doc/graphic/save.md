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
|svg|string|SVG图。|

返回值
```json
{
    "type": "类型",
    "sort": "顺序",
    "name": "名称",
    "label": "说明",
    "svg": "SVG图"
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。