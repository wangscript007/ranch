# 检索配置集

请求
- Service Key - ranch.chrome.query
- URI - /chrome/query

参数

|名称|类型|说明|
|---|---|---|
|key|string|引用key，支持模糊匹配。|
|name|string|名称，支持模糊匹配。|
|pageSize|int|每页显示记录数，小于等于0则默认20。|
|pageNum|int|当前显示页数。|

返回值
```json
{
  "count": "总记录数",
  "size": "每页显示记录数",
  "number": "当前显示页数",
  "list": [
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
  ]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
