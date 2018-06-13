# 修改文件名

请求
- Service Key - ranch.editor.media.name
- URI - /editor/media/name

参数

|名称|类型|说明|
|---|---|---|
|id|string|元素ID值。|
|editor|char(36)|编辑器ID值。|
|name|char(100)|文件名。|

返回值
```
{
  "user": "用户",
  "editor": "编辑器",
  "type": "类型：0-背景；1-图片；2-音频；3-视频",
  "url": "URL地址",
  "name": "文件名",
  "width": "图片宽",
  "height": "图片高",
  "time": "时间",
}
```