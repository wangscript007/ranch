# 重建搜索索引

请求
- Service Key - ranch.editor.reset-search-index
- URI - /editor/reset-search-index

参数

|名称|类型|必须|说明|
|---|---|---|---|
|id|char(36)|是|type。|

返回值
```
"异步ID"
```

> 异步ID参考[异步服务](../../ranch-base/doc/async.md)。

> 耗资源操作，建议仅在必须时使用；系统会默认每天凌晨自动重建索引。

> 默认索引保存位置为`/lucene`，可修改`tephra.lucene.root`配置更换。