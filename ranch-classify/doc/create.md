# 创建新分类

请求
- Service Key - ranch.classify.create
- URI - /classify/create

参数
- code 编码，必须。
- key 键。
- value 值，必须。
- name 名称，必须。

返回值
```json
{
    "id": "ID值",
    "code": "编码",
    "key": "键",
    "value": "值",
    "name": "名称"
}
```

> [扩展属性](json.md)

> [刷新缓存](refresh.md)

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
