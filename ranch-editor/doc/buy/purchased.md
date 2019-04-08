# 是否购买

请求
- Service Key - ranch.editor.buy.purchased
- URI - /editor/buy/purchased

参数

|名称|类型|必须|说明|
|---|---|---|---|
|editors|string|是|编辑器ID集，以逗号分割。|

返回值
```
{
    "editor-1": true/false,
    ...
    "editor-n": true/false
}
```