# 资源

属性

|属性|类型|说明|
|---|---|---|
|type|char(100)|分类。|
|sort|int|顺序。|
|name|char(100)|名称。|
|label|char(100)|说明。|
|uri|char(100)|资源URI地址。|
|size|int|文件大小。|
|width|int|图片宽。|
|height|int|图片高。|
|thumbnail|char(100)|缩略图URI地址。|
|author|char(100)|作者。|
|state|int|状态：0-待审核；1-审核通过；2-审核拒绝；3-已上架；4-已下架。|
|user|char(36)|用户。|
|time|datetime|时间。|

[检索](doc/query.md)

[已上架资源集](doc/onsale.md)

[保存](doc/save.md)

[审核通过](doc/pass.md)

[审核不通过](doc/reject.md)

[上架](doc/sale.md)

[下架](doc/nonsale.md)

[上传](doc/upload.md)

[删除](doc/delete.md)