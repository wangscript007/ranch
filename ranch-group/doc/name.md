# 修改群组名称

请求
- Service Key - ranch.group.name
- URI - /group/name

参数
- id ID值。
- name 名称。

返回值
```json
{
    "id": "ID值",
    "owner": "所有者ID",
    "name": "名称",
    "note": "公告",
    "member": "成员数",
    "audit": "新成员是否需要审核：0-否；1-是",
    "create": "创建时间"
}
```
