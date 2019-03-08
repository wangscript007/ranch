# 搜索标签

请求
- Service Key - ranch.editor.search-label
- URI - /editor/search-label

参数

|名称|类型|必须|说明|
|---|---|---|---|
|type|string|是|类型。|
|template|int|是|模板：1-模板；2-范文。|
|label|string|是|标签。|
|size|int|是|数量。|

返回值
```json
{
    "refresh": "是否刷新：true/false",
    "list": [
        {
            "template": "模板：0-否；1-模板；2-范文",
            "type": "类型",
            "sort": "顺序",
            "name": "名称",
            "label": "标签",
            "summary": "摘要",
            "width": "宽度",
            "height": "高度",
            "image": "预览图",
            "screenshot": "主截图",
            "total": "总根节点数",
            "modified": "已修改根节点数",
            "state": "状态：0-待审核；1-审核通过；2-审核拒绝；3-已上架；4-已下架；5-已删除",
            "source": "来源",
            "used": "被使用次数",
            "create": "创建时间",
            "modify": "修改时间"
        }
    ]
}
```

> 如果未检索到数据,将返回空JSON`{}`。
