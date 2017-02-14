# 创建新分类

请求
- Service Key - ranch.classify.create
- URI - /classify/create

参数
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
