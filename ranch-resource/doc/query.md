# 检索

请求
- Service Key - ranch.resource.query
- URI - /resource/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|type|char(100)|否|分类，为空则表示全部。|
|name|char(100)|否|名称，为空则表示全部。|
|label|char(100)|否|标签，为空则表示全部。|
|state|int|否|状态：-1-全部；0-待审核；1-审核通过；2-审核拒绝；3-已上架；4-已下架。|
|uid|char(100)|否|用户uid或id，为空则表示全部。|
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
            "type": "类型",
            "sort": "顺序",
            "name": "名称",
            "label": "说明",
            "uri": "资源URI地址",
            "size": "文件大小",
            "width": "图片宽",
            "height": "图片高",
            "thumbnail": "缩略图URI地址",
            "author": "作者",
            "state": "状态：0-待审核；1-审核通过；2-审核拒绝；3-已上架；4-已下架",
            "user": "用户",
            "time": "时间"
        }
    ]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。