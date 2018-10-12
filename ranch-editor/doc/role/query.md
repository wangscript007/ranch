# 检索

请求
- Service Key - ranch.editor.role.query
- URI - /editor/role/query

参数

|名称|类型|必须|说明|
|---|---|---|---|
|editor|char(36)|是|编辑器ID。|

返回值
```
[{
    "user": "用户",
    "editor": "编辑器",
    "type": "类型：0-所有者；1-可编辑；2-仅浏览",
    "password": "访问密码",
    "template": "编辑器模板",
    "etype": "编辑器类型",
    "state": "编辑器状态",
    "modify": "编辑器修改时间",
    "create": "创建时间"
}]
```