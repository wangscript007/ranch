# 通知配置

通知配置为JSON格式的数据，支持微服务和HTTP两种方式，并且允许两种方式同时通知：

```json
{
  "service": "微服务key",
  "http": "HTTP(S)请求URL地址",
  "params": "通知参数，通知时将原样推送给接收方"
}
```