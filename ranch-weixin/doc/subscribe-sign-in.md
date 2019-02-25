# 关注并登入

## 获取二维码URL

请求
- Service Key - ranch.weixin.subscribe-qr
- URI - /weixin/subscribe-qr

参数

无

返回值
```
url
```

## 轮询登入状态

请求
- Service Key - ranch.weixin.subscribe-sign-in
- URI - /weixin/subscribe-sign-in

参数

无

返回值
```json
{}
```

> 如果用户已登入，则返回微信信息；否则返回空JSON。

## 登入

调用用户[登入](../../ranch-user/doc/sign-in.md)接口，并设置`uid=password=subscribe-sign-in`。
