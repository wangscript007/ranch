# 保存

请求
- Service Key - ranch.last.save
- URI - /last/save

参数
- type 类型。
- 接受key=value形式的扩展数据。

返回值
```json
{
    "id": "ID值",
    "user": "用户ID值",
    "type": "类型",
    "time": "更新时间，格式yyyy-MM-dd HH:mm:ss",
    "millisecond": "更新时间戳，单位：毫秒"
}
```

> 如果存在扩展数据则扩展数据也将被返回。
