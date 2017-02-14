# 修改分类信息

请求
- Service Key - ranch.classify.modify
- URI - /classify/modify

参数
- id ID值。
- code 编码。
- name 名称。
- label 标签。

返回值
```json
{
    "id": "ID值",
    "code": "编码",
    "name": "名称",
    "label": "标签"
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
