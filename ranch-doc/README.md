# 文档

属性

|属性|类型|说明|
|---|---|---|
|author|char(36)|作者。|
|category|char(100)|类别。|
|sort|int|顺序。|
|subject|char(100)|标题。|
|image|char(100)|主图URI地址。|
|thumbnail|char(100)|缩略图URI地址。|
|summary|string|摘要。|
|label|string|标签。|
|type|char(100)|类型。|
|source|string|内容源。|
|content|string|内容。|
|json|string|扩展属性集。|
|read|int|阅读次数。|
|favorite|int|收藏次数。|
|comment|int|评论次数。|
|praise|int|点赞数。|
|score|int|得分。|
|time|datetime|更新时间。|
|audit|int|审核状态：0-待审核；1-审核通过；2-审核不通过。|
|auditRemark|char(100)|审核备注。|
|recycle|int|回收站：0-否，1-是。|

> 支持扩展自定义属性。

[检索文档信息集](doc/query.md)

[检索当前用户文档信息集](doc/query-by-author.md)

[获取文档信息](doc/find.md)

[获取文档信息集](doc/get.md)

[保存文档信息](doc/save.md)

[获取文档源内容](doc/source.md)

[检索已发布文档信息集](doc/index.md)

[搜索已发布文档信息集](doc/search.md)

[阅读文档](doc/read.md)

[阅读JSON](doc/read-json.md)

[增减收藏数](doc/favorite.md)

[增减评论数](doc/comment.md)

[点赞](doc/praise.md)

[刷新](doc/refresh.md)

[审核通过](../ranch-base/doc/audit-pass.md)

[审核不通过](../ranch-base/doc/audit-reject.md)

[检索回收站数据](../ranch-base/doc/recycle.md)

[删除到回收站](../ranch-base/doc/recycle-delete.md)

[从回收站还原](../ranch-base/doc/recycle-restore.md)

[配置参数](src/main/resources/doc.ranch.config)