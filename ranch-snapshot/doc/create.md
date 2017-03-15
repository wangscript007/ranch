# 创建新快照

请求
- Service Key - ranch.snapshot.create
- URI - /snapshot/create

参数
- data 数据。
- content 内容。

返回值
```text
"id"
```

> 内部服务接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
