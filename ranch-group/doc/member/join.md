# 加入群组

请求
- Service Key - ranch.group.member.join
- URI - /group/member/join

参数
- group 群组ID值。
- user 用户ID，为空则表示当前用户。
- reason 申请加入理由。
- introducer 推荐人成员ID。

返回值
```text
""
```

> 如果群组设置为不需要审核，则自动成为普通成员；否则为待审核状态，需管理员或群组审核通过后才能成为群组成员。

> 如果推荐人为管理员或群组创建者，则即使群组设置为需要审核，都将自动审核通过。
