# 微信PC端登入跳转

请求
- Service Key - ranch.user.sign-in
- URI - /user/sign-in

参数

|名称|类型|必须|说明|
|---|---|---|---|
|key|char(100)|是|微信引用KEY。|
|redirectUrl|string|是|返回跳转URL地址。|

返回

用户授权认证完成后，将回调redirectUrl地址，并附加以下参数：

|名称|类型|说明|
|---|---|---|
|state|string|success-认证成功；failure-认证失败。|
