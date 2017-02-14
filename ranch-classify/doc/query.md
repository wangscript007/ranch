# 检索分类信息集

请求
- Service Key - ranch.classify.query
- URI - /classify/query

参数
- code 编码前缀，会自动匹配【code+%】。
- pageSize 每页显示记录数。
- pageNum 当前显示页数。

返回值
```json
{
    "count": "总记录数",
    "size": "每页显示记录数",
    "number": "当前显示页数",
    "list": [
        {
            "id": "ID值",
            "code": "编码",
            "name": "名称",
            "label": "标签",
            "recycle":"回收站：0-否，1-是"
        }
    ]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
