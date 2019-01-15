# 检索编辑器信息集

请求
- Service Key - ranch.editor.query
- URI - /editor/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|user|string|否|用户ID，为空表示不限制。|
|uid|string|否|用户UID，为空表示不限制。|
|mobile|string|否|用户手机号，为空表示不限制。|
|email|string|否|用户Email，为空表示不限制。|
|nick|string|否|用户昵称，为空表示不限制。|
|template|int|否|模板：-1-全部；0-否；1-模板；2-范文。|
|type|string|否|类型，为空表示不限制。|
|name|string|否|名称，模糊匹配，为空表示不限制。|
|label|string|否|标签，模糊匹配，为空表示不限制。|
|modified|int|否|最小已修改根节点数，-1表示不限制。|
|states|string|否|状态集，多个状态以逗号分隔，为空表示不限制。|
|createStart|string|否|创建开始日期，格式：yyyy-MM-dd，为空表示不限制。|
|createEnd|string|否|创建结束日期，格式：yyyy-MM-dd，为空表示不限制。|
|modifyStart|string|否|编辑开始日期，格式：yyyy-MM-dd，为空表示不限制。|
|modifyEnd|string|否|编辑结束日期，格式：yyyy-MM-dd，为空表示不限制。|
|order|string|否|排序规则：hot-热门；newest-最新（默认）；none-不排序。|
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
            "template": "模板：0-否；1-模板；2-范文",
            "type": "类型",
            "sort": "顺序",
            "name": "名称",
            "label": "标签",
            "summary": "摘要",
            "width": "宽度",
            "height": "高度",
            "image": "预览图",
            "screenshot": "主截图",
            "total": "总根节点数",
            "modified": "已修改根节点数",
            "state": "状态：0-待审核；1-审核通过；2-审核拒绝；3-已上架；4-已下架；5-已删除",
            "source": "来源",
            "used": "被使用次数",
            "create": "创建时间",
            "modify": "修改时间",
            "owner": {}
        }
    ]
}
```

> `owner`为创建者用户信息。

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
