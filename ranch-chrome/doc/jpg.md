# 导出JPEG图片

请求
- Service Key - ranch.chrome.jpg
- URI - /chrome/jpg

参数

|名称|类型|说明|
|---|---|---|
|key|char(100)|引用key，必须。|
|url|string|要导出的页面URL地址，必须。|
|x|int|图片X位置，小于等于0则使用配置值。|
|y|int|图片Y位置，小于等于0则使用配置值。|
|width|int|宽度，小于等于0则使用配置值。|
|height|int|高度，小于等于0则使用配置值。|
|wait|int|等待时长，单位：秒；小于等于0则使用配置值。|

返回值
```
JPEG图片二进制流
```
