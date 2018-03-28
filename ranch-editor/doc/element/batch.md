# 批量操作

请求
- Service Key - ranch.editor.elemet.batch
- URI - /editor/element/batch

参数
```json
[
    {
        "operation":"save/sort/delete",
        parameters
    }
]
```
> `parameters`为相应接口需要的参数。

返回值
```json
[
    result
]
```
> `result`为各接口返回的结果；如果参数验证不通过则返回空集`[]`。