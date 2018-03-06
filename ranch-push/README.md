# 推送

推送模块主要实现短信、Email、APP等推送配置与发送功能。

属性

|属性|类型|说明|
|---|---|---|
|key|char(100)|引用键。|
|sender|char(100)|推送器，可选值见下表。|
|appCode|char(100)|APP编码。|
|subject|char(100)|标题。|
|content|string|内容。|
|template|char(100)|模板ID。|
|name|char(100)|发送者名称。|
|args|string|默认参数集，JSON格式。|
|state|int|状态：0-待审核；1-使用中。|
|time|timestamp|最后更新时间。|

sender可选值

|属性|说明|
|---|---|
|smtp|SMTP推送。|
|sms.aliyun|阿里云（大于）短信。|

[检索推送配置集](doc/query.md)

[保存配置](doc/save.md)

[修改状态](doc/state.md)

[删除配置](doc/delete.md)

[推送](doc/send.md)

## 参数设置与获取

调用[推送](doc/send.md)接口时，可以传递JSON格式的参数给模板，如：
```json
{
    "name": "ranch"
}
```
则在设置标题和内容时，可以使用`${data.name}`获得该参数值。如：
```text
Hello ${data.name}
```
发送时将输出为：
```text
Hello ranch
```

# iOS推送配置

配置iOS推送的证书、密钥等信息。

属性

|属性|类型|说明|
|---|---|---|
|appCode|char(100)|APP编码。|
|p12|string|证书，BASE64编码。|
|password|char(100)|证书密码。|
|destination|int|目的地：0-开发；1-正式。|
|time|datetime|更新时间。|

[检索](doc/ios/query.md)

[保存](doc/ios/save.md)

[删除](doc/ios/delete.md)

# 阿里云推送配置

配置阿里云APP推送密钥等信息。

属性

|属性|类型|说明|
|---|---|---|
|appCode|char(100)|APP编码。|
|keyId|char(100)|KEY ID。|
|keySecret|char(100)|KEY密钥。|
|appKey|char(100)|APP KEY。|
|time|datetime|更新时间。|

[检索](doc/aliyun/query.md)

[保存](doc/aliyun/save.md)

[删除](doc/aliyun/delete.md)

# 推送日志

保存推送数据、状态等信息。

属性

|属性|类型|说明|
|---|---|---|
|user|char(36)|用户。|
|receiver|char(100)|接收地址。|
|appCode|char(100)|APP编码。|
|sender|char(100)|推送器。|
|push|string|推送配置，JSON格式。|
|args|stirng|参数集，JSON格式。|
|time|datetime|时间。|
|state|int|状态：0-新建；1-已推送；2-已阅读；3-推送失败。|

[检索](doc/log/query.md)

[检索个人](doc/log/uquery.md)

[未读数](doc/log/unread.md)

[最新未读](doc/log/unread-newest.md)

[阅读](doc/log/read.md)

[阅读全部](doc/log/reads.md)
