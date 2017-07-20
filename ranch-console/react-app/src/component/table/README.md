# Table

```html
<Table
 message=""
 headers=""
 names=""
 list=""/>
```

|名称|类型|说明|
|---|---|---|
|message|object|消息对象，可选。|
|headers|array|标题集，必须。|
|names|array|属性名称集，必须。|
|list|array|数据集。|

## headers
headers为json对象数据，对象须包含label属性。

demo
```
var headers = [{ label: "标题1" }, { label: "project-name" }];
var names = ["x", "y"];
var list = [{ x: "X", y: "Y" }, { x: "哈哈", y: "Y值" }, { x: "X", y: "Y" }, { x: "哈哈", y: "Y值" }];

<Table headers={headers} names={names} list={list} />
```