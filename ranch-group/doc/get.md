# 获取群组信息集

请求
- Service Key - ranch.group.get
- URI - /group/get

参数
- ids ID集，多个间以逗号分隔。

返回值
```json
{
    "id-value": {
        "id": "ID值",
        "owner": "所有者ID",
        "name": "名称",
        "portrait": "头像",
        "note": "公告",
        "member": "成员数",
        "audit": "新成员是否需要审核：0-否；1-是",
        "create": "创建时间"
    }
}
```

> 内部服务接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
