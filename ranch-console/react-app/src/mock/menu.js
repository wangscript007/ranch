class Menu {
    mock(mock) {
        mock.put("/console/menu", "", [{
            "name": "系统设置",
            "items": [{
                "name": "用户管理",
                "service": "ranch.user.query"
            }, {
                "name": "账户管理",
                "service": "ranch.account.query"
            }]
        }]);
    }
}

export default new Menu();