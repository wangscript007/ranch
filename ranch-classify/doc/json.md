# 扩展属性

分类信息模块支持自定义扩展属性，在新增、修改时只需添加上需要的属性与值，便可被持久化。如：
```text
curl -d "code=123&key=abc&name=classify&custom=hello&say=hi" http://127.0.0.1:8080/classify/create
{
    "code": 0,
    "data": {
        "code": "123",
        "custom": "hello",
        "name": "classify",
        "say": "hi",
        "id": "82991f26-2213-4531-8996-7fb24d62304c",
        "key": "abc"
    }
}

curl -d "code=123&key=abc&name=classify&custom=hello2&u=xyz&id=82991f26-2213-4531-8996-7fb24d62304c" http://127.0.0.1:8080/classify/modify
{
    "code": 0,
    "data": {
        "code": "123",
        "u": "xyz",
        "custom": "hello2",
        "name": "classify",
        "say": "hi",
        "id": "82991f26-2213-4531-8996-7fb24d62304c",
        "key": "abc"
    }
}

curl -d "ids=82991f26-2213-4531-8996-7fb24d62304c" http://127.0.0.1:8080/classify/get
{
    "code": 0,
    "data": {
        "82991f26-2213-4531-8996-7fb24d62304c": {
            "code": "123",
            "u": "xyz",
            "custom": "hello2",
            "name": "classify",
            "say": "hi",
            "id": "82991f26-2213-4531-8996-7fb24d62304c",
            "key": "abc"
        }
    }
}

curl -d "pageSize=20&pageNum=1" http://127.0.0.1:8080/classify/query
{
    "code": 0,
    "data": {
        "number": 1,
        "size": 20,
        "pageStart": 1,
        "pageEnd": 1,
        "count": 1,
        "page": 1,
        "list": [
            {
                "code": "123",
                "u": "xyz",
                "custom": "hello2",
                "name": "classify",
                "say": "hi",
                "id": "82991f26-2213-4531-8996-7fb24d62304c",
                "key": "abc"
            }
        ]
    }
}
```

移除扩展属性时，只需在更新接口参数中添加“-要删除扩展属性名”，如：
```text
curl -d "code=123&key=abc&name=classify&custom=hello2&-u&id=82991f26-2213-4531-8996-7fb24d62304c" http://127.0.0.1:8080/classify/modify
{
    "code": 0,
    "data": {
        "code": "123",
        "custom": "hello2",
        "name": "classify",
        "say": "hi",
        "id": "82991f26-2213-4531-8996-7fb24d62304c",
        "key": "abc"
    }
}
```
> “-u”表示删除扩展属性“u”。
