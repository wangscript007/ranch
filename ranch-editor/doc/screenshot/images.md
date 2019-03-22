# 截图集

请求
- Service Key - ranch.editor.screenshot.images
- URI - /editor/screenshot/images

参数

|名称|类型|必须|说明|
|---|---|---|---|
|editor|string|是|编辑器ID。|

返回值
```
"异步ID"
```

> 异步ID参考[异步服务](../../../ranch-base/doc/async.md)。

> 需要配置`ranch.editor.screenshot.capture`参数，系统调用时会拼接`?sid={当前用户Session ID}&editor={编辑器ID}&page={页面ID，主页面为空""}`参数。
