# 删除

请求
- Service Key - ranch.editor.elemet.delete
- URI - /editor/element/delete

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|string|是|元素ID值。|
|editor|char(36)|是|编辑器ID值。|
|parent|char(36)|否|父元素ID值，为空则表示根元素。|
|modify|long|是|修改时间。|

返回值
```
""
```