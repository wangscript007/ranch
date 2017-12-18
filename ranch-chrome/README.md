# Chrome

Chrome模块使用Chrome浏览器的DevTools的远程调试接口，实现将页面导出为PDF及图片的功能。

属性

|属性|类型|说明|
|---|---|---|
|key|char(100)|引用key。|
|name|char(100)|名称。|
|x|int|图片X位置。|
|y|int|图片Y位置。|
|width|int|宽度。|
|height|int|高度。|
|pages|char(100)|页面集，导出PDF时指定。|
|wait|int|等待时长，单位：秒。|
|filename|char(100)|文件名，导出PDF时的默认文件名。|

[检索配置集](doc/query.md)

[保存](doc/save.md)

[删除](doc/delete.md)

[导出PDF](doc/pdf.md)

[导出PNG图片](doc/png.md)

[导出JPEG图片](doc/jpg.md)

[内存检查](doc/memory.md)