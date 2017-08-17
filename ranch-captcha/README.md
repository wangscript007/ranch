# 验证码

验证码模块主要实现验证码的配置与校验。

属性

|属性|类型|说明|
|---|---|---|
|key|char(100)|引用key，必须。|
|name|char(100)|名称。|
|width|int|宽度，必须。|
|height|int|高度，必须。|
|fontMin|int|最小字号，必须。|
|fontMax|int|最大字号，必须。|
|chars|char(100)|字符集，必须。|
|length|int|字符数，必须。|
|background|int|是否使用背景图片：0-否；1-是。|

[检索验证码集](doc/query.md)

[保存验证码配置](doc/save.md)

[删除验证码配置](doc/delete.md)

[生成验证码图片](doc/image.md)

[校验验证码](doc/validate.md)