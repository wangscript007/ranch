# 连接 & 关联

主要用于关联两个对象的ID，如保存用户对某一篇文章或消息的阅读状态。

属性

|属性|类型|说明|
|---|---|---|
|type|char(100)|类型，必须。|
|id1|char(36)|关联ID1，必须。|
|id2|char(36)|关联ID2，必须。|
|time|string|更新时间。|
> 支持保存、返回自定义的属性。

[检索关联集](doc/query.md)

[统计关联数](doc/count.md)

[判断关联数据是否存在](doc/exists.md)

[查找](doc/find.md)

[保存](doc/save.md)

[删除](doc/delete.md)
