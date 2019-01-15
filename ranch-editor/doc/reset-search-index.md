# 重建搜索索引

请求
- Service Key - ranch.editor.reset-search-index
- URI - /editor/reset-search-index

参数

|名称|类型|必须|说明|
|---|---|---|---|
|type|char(100)|是|类型。|
|template|int|是|模板：1-模板；2-范文。|

返回值
```
"异步ID"
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。

> 异步ID参考[异步服务](../../ranch-base/doc/async.md)。

> 耗资源操作，建议仅在必须时使用；系统会默认每天凌晨自动重建索引。

> 默认索引保存位置为`/lucene`，可修改`tephra.lucene.root`配置更换。