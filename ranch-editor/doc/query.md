# 检索编辑器信息集

请求
- Service Key - ranch.editor.query
- URI - /editor/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|mobile|string|否|用户手机号，为空表示不限制。|
|email|string|否|用户Email，为空表示不限制。|
|nick|string|否|用户昵称，为空表示不限制。|
|type|string|否|类型，为空表示不限制。|
|name|string|否|名称，模糊匹配，为空表示不限制。|
|keyword|string|否|关键词，模糊匹配，为空表示不限制。|
|state|int|否|状态：-1-全部；0-待审核；1-审核通过；2-审核拒绝；3-已上架；4-已下架。|
|createStart|string|否|创建开始日期，格式：yyyy-MM-dd，为空表示不限制。|
|createEnd|string|否|创建结束日期，格式：yyyy-MM-dd，为空表示不限制。|
|modifyStart|string|否|编辑开始日期，格式：yyyy-MM-dd，为空表示不限制。|
|modifyEnd|string|否|编辑结束日期，格式：yyyy-MM-dd，为空表示不限制。|
|pageSize|int|否|每页显示记录数，默认20。|
|pageNum|int|否|当前显示页数。|

返回值
```json
{
    "count": "总记录数",
    "size": "每页显示记录数",
    "number": "当前显示页数",
    "list": [
        {
            "template": "模板：0-否；1-是",
            "type": "类型",
            "sort": "顺序",
            "name": "名称",
            "keyword": "关键词",
            "width": "宽度",
            "height": "高度",
            "image": "预览图",
            "state": "状态：0-待审核；1-审核通过；2-审核拒绝；3-已上架；4-已下架",
            "create": "创建时间",
            "modify": "修改时间"
        }
    ]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
