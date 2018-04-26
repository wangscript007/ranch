# 日志服务

日志服务主要实现将关键日志持久化到数据库，以支持检索。

属性

|属性|类型|说明|
|---|---|---|
|key|char(100)|键。|
|p0|char(100)|参数0。|
|p1|char(100)|参数1。|
|p2|char(100)|参数2。|
|p3|char(100)|参数3。|
|p4|char(100)|参数4。|
|p5|char(100)|参数5。|
|p6|char(100)|参数6。|
|p7|char(100)|参数7。|
|p8|char(100)|参数8。|
|p9|char(100)|参数9。|
|state|int|状态。|
|time|timestamp|时间。|

## 记录日志

```java
package org.lpw.ranch.logger;

import com.alibaba.fastjson.JSONObject;

/**
 * 日志服务。
 *
 * @author lpw
 */
public interface LoggerService {
    /**
     * 创建日志。
     *
     * @param key 键。
     * @param ps  参数集：不超过10个，超过忽视；每个字符串长度不可超过100字符，超过截断。
     */
    void create(String key, String... ps);
}
```

## 检索日志集

请求
- Service Key - ranch.logger.query
- URI - /logger/query

参数

|名称|类型|说明|
|---|---|---|
|key|char(100)|键，为空表示全部。|
|state|int|状态，-1表示全部。|
|start|string|开始时间，格式：yyyy-MM-dd HH:mm:ss。为空表示全部。|
|end|string|结束时间，格式：yyyy-MM-dd HH:mm:ss。为空表示全部。|
|pageSize|int|每页显示记录数，默认20。|
|pageNum|int|当前显示页数。|

返回

```json
{
    "count": "总记录数",
    "size": "每页显示记录数",
    "number": "当前显示页数",
    "list": [
        {
            "id": "ID值",
            "key": "键",
            "p0": "参数0",
            "p1": "参数1",
            "p2": "参数2",
            "p3": "参数3",
            "p4": "参数4",
            "p5": "参数5",
            "p6": "参数6",
            "p7": "参数7",
            "p8": "参数8",
            "p9": "参数9",
            "state": "状态",
            "time": "时间"
        }
    ]
}
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。

## 设置状态

请求
- Service Key - ranch.logger.state
- URI - /logger/state

参数

|名称|类型|说明|
|---|---|---|
|id|char(36)|ID值。|
|state|int|状态。|

返回值
```
""
```

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
