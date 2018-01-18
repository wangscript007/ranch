# 保存

请求
- Service Key - ranch.device.save
- URI - /device/save

参数

|名称|类型|说明|
|---|---|---|
|user|char(36)|用户ID，空使用当前用户。|
|appCode|char(100)|APP编码。|
|type|char(100)|类型：android、ios。|
|macId|char(100)|Mac ID。|
|version|char(100)|版本号。|

返回值
```json
{
  "id": "ID值",
  "user": "用户",
  "appCode": "APP编码",
  "type": "类型：android、ios",
  "macId": "Mac ID",
  "version": "版本号",
  "time": "时间"
}
```
