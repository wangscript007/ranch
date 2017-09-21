# 保存

请求
- Service Key - ranch.chrome.save
- URI - /chrome/save

参数

|名称|类型|说明|
|---|---|---|
|key|char(100)|引用key。|
|name|char(100)|名称。|
|x|int|图片X位置。|
|y|int|图片Y位置。|
|width|int|宽度。|
|height|int|高度。|
|pages|char(100)|页面集，导出PDF时指定。|
|wait|int|等待时长，单位：秒。|
|filename|char(100)|文件名，导出PDF时的默认文件名。|

返回值
```json
{
  "id": "ID值",
  "key": "引用key",
  "name": "名称",
  "x": "图片X位置",
  "y": "图片Y位置",
  "width": "宽度",
  "height": "高度",
  "pages": "页面集",
  "wait": "等待时长，单位：秒",
  "filename": "文件名"
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
