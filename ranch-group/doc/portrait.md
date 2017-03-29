# 修改群组头像

请求
- Service Key - ranch.group.portrait
- URI - /group/portrait

参数
- id ID值。
- portrait 头像。

返回值
```json
{
    "id": "ID值",
    "owner": "所有者ID",
    "name": "名称",
    "portrait": "头像",
    "note": "公告",
    "member": "成员数",
    "audit": "新成员是否需要审核：0-否；1-是",
    "create": "创建时间"
}
```

> 头像上传参考[上传文件](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl-http/doc/upload.md)说明，其中${key}=ranch.group.portrait。

> 上传后头像将同时保存一张80*80的缩略图。
