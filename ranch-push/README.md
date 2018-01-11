# 推送

推送模块主要实现短信、Email、APP等推送配置与发送功能。

属性

|属性|类型|说明|
|---|---|---|
|key|char(100)|引用键。|
|sender|char(100)|推送器，可选值见下表。|
|subject|char(100)|标题。|
|content|char(100)|内容。|
|template|char(100)|模板ID。|
|name|char(100)|发送者名称。|
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
