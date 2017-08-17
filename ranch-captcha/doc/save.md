# 保存验证码配置

请求
- Service Key - ranch.captcha.save
- URI - /captcha/save

参数

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

返回值
```json
{
    "id": "ID值",
    "key": "引用key",
    "name": "名称",
    "width": "宽度",
    "height": "高度",
    "fontMin": "最小字号",
    "fontMax": "最大字号",
    "chars": "字符集",
    "length": "字符数",
    "background": "是否使用背景图片：0-否；1-是"
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
