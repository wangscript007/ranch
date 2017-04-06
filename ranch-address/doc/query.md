# 检索当前用户地址集

请求
- Service Key - ranch.address.query
- URI - /address/query

返回值
```json
[
    {
        "id": "ID值",
        "user": "用户ID",
        "region": "行政区字典ID",
        "detail": "详细地址",
        "postcode": "邮政编码",
        "latitude": "GPS纬度",
        "longitude": "GPS经度",
        "label": "标签",
        "major": "默认地址：0-否；1-是",
        "time": "时间"
    }
]
```
