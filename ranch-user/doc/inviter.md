# 邀请人

请求
- Service Key - ranch.user.inviter
- URI - /user/inviter

参数

|名称|类型|必须|说明|
|---|---|---|---|
|inviter|char(8)|否|邀请人code，为空则不缓存。|

返回值
```
'code'
```
> 返回当前缓存的邀请人`code`值。
