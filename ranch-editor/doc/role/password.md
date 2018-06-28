# 设置访问密码

请求
- Service Key - ranch.editor.role.password
- URI - /editor/role/password

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|char(36)|是|ID值。|
|editor|char(36)|是|编辑器ID。|
|password|char(100)|否|访问密码，为空表示不设置。|

返回值
```
""
```