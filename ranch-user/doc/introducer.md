# 介绍人

请求
- Service Key - ranch.user.introducer
- URI - /user/introducer

参数

|名称|类型|必须|说明|
|---|---|---|---|
|introducer|char(8)|否|介绍人code，为空则不缓存。|

返回值
```
'code'
```
> 返回当前缓存的介绍人`code`值。
