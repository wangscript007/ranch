# 修改当前用户头像

请求
- URI - /tephra/ctrl-http/upload

参数

|名称|类型|说明|
|---|---|---|
|ranch.user.portrait|string|头像文件。|

返回值
```text
uri
```
> 如果上传成功则返回图片URI地址，否则返回空字符串。
