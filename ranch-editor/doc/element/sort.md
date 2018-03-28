# 排序

请求
- Service Key - ranch.editor.elemet.sort
- URI - /editor/element/sort

参数

|名称|类型|说明|
|---|---|---|
|ids|string|元素ID值集，按顺序以逗号分隔。|
|editor|char(36)|编辑器ID值。|
|parent|char(36)|父元素ID值，为空则表示根元素。|
|modifies|string|修改时间集，按顺序以逗号分隔。|

返回值
```
""
```