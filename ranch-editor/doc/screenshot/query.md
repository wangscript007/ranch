# 检索截图集

请求
- Service Key - ranch.editor.screenshot.query
- URI - /editor/screenshot/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|editor|string|是|编辑器ID。|

返回值
```json
[{
    "editor": "编辑器",
    "index": "序号",
    "page": "页面",
    "uri": "URI地址"
}]
```
