# 统计关联数

请求
- Service Key - ranch.link.count
- URI - /link/count

参数

|名称|类型|说明|
|---|---|---|
|type|char(100)|类型，必须。|
|id1|char(36)|关联ID1，如果为空则统计ID2，不为空则统计ID1。|
|id2|char(36)|关联ID2。|

返回值
```
number
```
> 返回统计数量。
