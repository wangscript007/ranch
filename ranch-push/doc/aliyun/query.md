# 检索

请求
- Service Key - ranch.push.aliyun.query
- URI - /push/aliyun/query

参数

|名称|类型|说明|
|---|---|---|
|pageSize|int|每页显示记录数，小于等于0则默认20。|
|pageNum|int|当前显示页数。|

返回值
```json
{
  "count": "总记录数",
  "size": "每页显示记录数",
  "number": "当前显示页数",
  "list": [
    {
      "id": "ID值",
      "appCode": "APP编码",
      "keyId": "KEY ID",
      "keySecret": "KEY密钥",
      "appKey": "APP KEY",
      "time": "更新时间"
    }
  ]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
