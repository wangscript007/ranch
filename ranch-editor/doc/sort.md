# 排序

请求
- Service Key - ranch.editor.sort
- URI - /editor/sort

参数

|名称|类型|必须|说明|
|---|---|---|---|
|type|string|是|类型。|
|ids|string|是|ID集，以逗号分隔。|
|sorts|string|是|顺序集，以逗号分隔。|

返回值
```
""
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
