# 获取快照集

请求
- Service Key - ranch.snapshot.get
- URI - /snapshot/get

参数
- ids 要获取的快照ID集，多个ID间以逗号分隔。

返回值
```json
{
  "id-1": {
    "id": "ID值",
    "data":"数据",
    "content":"内容",
    "time":"时间"
  },
  "id-n": {
    "id": "ID值",
    "data":"数据",
    "content":"内容",
    "time":"时间"
  }
}
```

> 内部服务接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
