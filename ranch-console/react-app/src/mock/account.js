class Account {
    mock(mock) {
        this.query(mock);
    }

    query(mock) {
        mock.put("/console/meta", "ranch.account.query", {
            page: "grid",
            cols: [{
                label: "用户",
                name: "user"
            }, {
                label: "所有者",
                name: "owner"
            }, {
                label: "余额",
                name: "balance",
                type: "number"
            }, {
                label: "存入",
                name: "deposit",
                type: "number"
            }, {
                label: "取出",
                name: "withdraw",
                type: "number"
            }]
        });

        var list = [];
        for (var i = 0; i < 10; i++) {
            list[i] = {
                id: "id " + i,
                user: "user " + i,
                owner: "owner " + i,
                balance: i,
                deposit: i * 2,
                withdraw: i * 3
            };
        }
        mock.put("/console/service", "ranch.account.query", {
            list: list
        });
    }
}

export default new Account();