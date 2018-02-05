# 审核不通过

请求
- Service Key - ${module-name}.reject
- URI - /${module-path}/reject
> ${module-name}为模块名称。

> ${module-path}为模块路径。

参数
- ids ID集，即要审核的ID数组，以逗号分隔。
- auditRemark 审核备注，填写拒绝理由，必须。

> 后台管理接口，需验证[请求参数签名](https://github.com/heisedebaise/tephra/blob/master/tephra-ctrl/doc/sign.md)。
