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
