# 检索绑定信息集

请求
- Service Key - ranch.device.query
- URI - /device/query

参数

|名称|类型|说明|
|---|---|---|
|user|char(36)|用户ID，空则不限制。|
|appCode|char(100)|APP编码，空则不限制。|
|type|char(100)|类型：android、ios，空则不限制。|
|macId|char(100)|Mac ID，空则不限制。|
|version|char(100)|版本号，空则不限制。|
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
      "user": "用户",
      "appCode": "APP编码",
      "type": "类型：android、ios",
      "macId": "Mac ID",
      "version": "版本号",
      "time": "时间"
    }
  ]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
