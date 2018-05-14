# 编辑器

属性

|属性|类型|说明|
|---|---|---|
|copy|char(36)|复制源。|
|type|char(100)|类型。|
|sort|int|顺序，默认0，降序。|
|name|char(100)|名称。|
|keyword|char(100)|关键词。|
|width|int|宽度。|
|height|int|高度。|
|image|char(100)|预览图。|
|create|datetime|创建时间。|
|modify|long|修改时间。|

> 支持扩展自定义属性。

[检索当前用户编辑器信息集](doc/query-user.md)

[查找](doc/find.md)

[保存](doc/save.md)

[复制](doc/copy.md)

## 角色

属性

|属性|类型|说明|
|---|---|---|
|user|char(36)|用户。|
|editor|char(36)|编辑器。|
|type|int|类型：0-所有者；1-可编辑；2-仅浏览。|

[删除](doc/role/delete.md)

## 用户媒体

属性

|属性|类型|说明|
|---|---|---|
|user|char(36)|用户。|
|editor|char(36)|编辑器。|
|type|int|类型：0-背景；1-图片；2-音频；3-视频。|
|url|char(100)|URL地址。|
|name|char(100)|文件名。|
|time|datetime|时间。|

[检索](doc/media/query.md)

[上传](doc/media/save.md)

[修改文件名](doc/media/name.md)

[删除](doc/media/delete.md)

## 元素

属性

|属性|类型|说明|
|---|---|---|
|editor|char(36)|编辑器。|
|parent|char(36)|父元素。|
|sort|int|顺序。|
|create|datetime|创建时间。|
|modify|long|修改时间。|

> 支持扩展自定义属性。

[检索](doc/element/query.md)

[保存](doc/element/save.md)

[排序](doc/element/sort.md)

[删除](doc/element/delete.md)

[批量操作](doc/element/batch.md)

## 日志

属性

|属性|类型|说明|
|---|---|---|
|editor|char(36)|编辑器。|
|parent|char(36)|父元素。|
|element|char(36)|元素。|
|sort|int|顺序。|
|type|char(100)|类型。|
|x|int|X位置。|
|y|int|Y位置。|
|width|int|宽度。|
|height|int|高度。|
|create|datetime|创建时间。|
|modify|long|修改时间。|
|operation|int|操作：0-新增；1-修改；2-删除。|
|time|datetime|时间。|
