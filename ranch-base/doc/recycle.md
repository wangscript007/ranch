# 检索回收站数据

请求
- Service Key - ${module-name}.recycle
- URI - /${module-path}/recycle
> ${module-name}为模块名称。

> ${module-path}为模块路径。

参数
- pageSize 每页显示记录数。
- pageNum 当前显示页数。

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
