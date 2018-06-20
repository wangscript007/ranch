# 分类 & 数据字典

分类&数据字典模块主要提供字典数据的维护与检索。


属性

|属性|类型|说明|
|---|---|---|
|code|char(100)|编码。|
|key|char(100)|键。|
|value|char(100)|值。|
|name|char(100)|名称。|

> code+key必须全局唯一。

[检索分类信息集](doc/query.md)

[检索分类信息树](doc/tree.md)

[获取分类信息集](doc/get.md)

[通过编码与值获取分类信息](doc/find.md)

[检索分类信息集](doc/list.md)

[保存分类信息](doc/save.md)

[上传图片](doc/upload.md)

[刷新缓存](doc/refresh.md)

[检索回收站数据](../ranch-base/doc/recycle.md)

[删除到回收站](../ranch-base/doc/recycle-delete.md)

[从回收站还原](../ranch-base/doc/recycle-restore.md)

[配置参数](src/main/resources/classify.ranch.config)
