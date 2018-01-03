# 推送

推送模块主要实现短信、Email、APP等推送配置与发送功能。

属性

|属性|类型|说明|
|---|---|---|
|key|char(100)|引用键。|
|subject|char(100)|标题。|
|content|char(100)|内容。|
|state|int|状态：0-待审核；1-使用中。|
|time|timestamp始时间。|

[检索推送配置集](doc/query.md)

