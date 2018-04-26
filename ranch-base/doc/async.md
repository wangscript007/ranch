# 异步服务

异步服务主要提供异步队列服务，并可获取执行状态。

属性

|属性|类型|说明|
|---|---|---|
|key|char(100)|引用KEY。|
|parameter|string|参数。|
|result|string|结果。|
|state|int|状态：0-进行中；1-已完成；2-异常；3-超时。|
|begin|datetime|开始时间。|
|finish|datetime|结束时间。|
|timeout|datetime|超时时间。|

## 提交

```java
package org.lpw.ranch.async;

/**
 * @author lpw
 */
public interface AsyncService {
    /**
     * 提交。
     *
     * @param key       引用KEY。
     * @param parameter 参数。
     * @param timeout   超时时长，单位：分钟。
     * @param runnable  执行处理器。
     * @return 异步ID。
     */
    String submit(String key, String parameter, int timeout, Callable<String> callable);
}
```

## 查询状态

请求
- Service Key - ranch.async.find
- URI - /async/find

参数

|名称|类型|说明|
|---|---|---|
|id|char(36)|ID值。|

返回
```json
{
    "state": "状态：0-进行中；1-已完成；2-异常；3-超时",
    "result": "结果，仅当状态为1时返回"
}
```