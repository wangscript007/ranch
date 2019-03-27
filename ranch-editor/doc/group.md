# 修改分组

请求
- Service Key - ranch.editor.group
- URI - /editor/group

参数

|名称|类型|必须|说明|
|---|---|---|---|
|oldGroup|char(100)|否|旧名称。|
|newGroup|char(100)|否|新名称。|

返回值
```
""
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。