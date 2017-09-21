# 导出PDF

请求
- Service Key - ranch.chrome.pdf
- URI - /chrome/pdf

参数

|名称|类型|说明|
|---|---|---|
|key|char(100)|引用key，必须。|
|url|string|要导出的页面URL地址，必须。|
|width|int|宽度，小于等于0则使用配置值。|
|height|int|高度，小于等于0则使用配置值。|
|pages|char(100)|页面集，为空则使用配置值。|
|wait|int|等待时长，单位：秒；小于等于0则使用配置值。|
|filename|char(100)|文件名，为空则使用配置值。|

返回值
```
PDF文件二进制流
```
