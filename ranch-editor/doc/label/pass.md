# 修改名称

请求
- Service Key - ranch.editor.label.rename
- URI - /editor/label/rename

参数

|名称|类型|必须|说明|
|---|---|---|---|
|oldName|char(100)|是|旧名称。|
|newName|char(100)|是|新名称。|

返回值
```
""
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。