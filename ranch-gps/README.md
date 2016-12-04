# GPS组件

## 获取GPS坐标对应的地址信息

地址：
- Service Key - ranch.gps.address
- URI - /gps/address

参数：
- lat - 纬度坐标值。
- lng - 经度坐标值。

返回：
```json
{
  "code" : 0,
  "data" : {
    "address" : "详细地址",
    "component" : {
      "nation" : "国家",
      "province" : "省",
      "city" : "市",
      "district" : "区",
      "street" : "街道",
      "street_number" : "门牌号"
    },
    "adcode" : "行政编号"
  }
}
```

示例：
```bash
curl -d "lat=39.917266&lng=116.397140" http://localhost:8080/gps/address
{"code":0,"data":{"address":"北京市东城区东华门大街","component":{"nation":"中国","province":"北京市","city":"北京市","district":"东城区","street":"东华门大街","street_number":""},"adcode":"110101"}}
```
