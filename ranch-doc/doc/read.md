# 阅读文档

请求
- Service Key - ranch.doc.read
- URI - /doc/read

参数
- id ID值。
- html 是否添加<html>标签：true-添加；其他-不添加。
- css 样式表名称集，仅当html=true时有效，css名称必须存在于/css/${css}.css。

返回值
```html
<#if data.html>
<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>${data.model.subject!""}</title>
    <#if data.css??>
    <#list data.css as css>
    <link type="text/css" rel="stylesheet" href="/css/${css}.css"/>
    </#list>
    </#if>
</head>
<body>
${data.html!""}
</body>
</html>
<#else>
${data.content}
</#if>
```
