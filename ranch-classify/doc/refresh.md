# 刷新缓存

分类与数据字典通常用于检索，因此会对检索结果进行缓存，并且每天会自动更新一次缓存。如果数据有更新并且希望数据立即生效，可执行此接口以清空现有缓存数据，使新数据生效。

参数

无

请求
- Service Key - ranch.classify.refresh
- URI - /classify/refresh

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
