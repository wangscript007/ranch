# 缓存

## 删除

请求
- Service Key - ranch.cache.remove
- URI - /cache/remove

参数

|名称|类型|必须|说明|
|---|---|---|---|
|type|string|否|缓存器，为空表示默认缓存器。|
|key|string|是|缓存key。|

返回
```
""
```