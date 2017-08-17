# 检索验证码集

请求
- Service Key - ranch.captcha.query
- URI - /captcha/query

参数

无

返回值
```json
[
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
]
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
