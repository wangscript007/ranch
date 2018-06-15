# 获取二维码

请求
- Service Key - ranch.weixin.wxa-code-unlimit
- URI - /weixin/wxa-code-unlimit

参数

|名称|类型|说明|
|---|---|---|
|key|string|加密算法的初始向量。|
|scene|string|场景。|
|page|string|页面。|
|width|int|宽度。|
|autoColor|boolean|自动配置线条颜色。|
|lineColor|string|线条颜色。|
|hyaline|boolean|是否透明底色。|

返回
```
"图片URI"
```
