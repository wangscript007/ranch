class Account {
    mock(mock) {
        this.query(mock);
    }

    query(mock) {
        mock.put("/console/meta", "ranch.account.query", {
            page: "grid",
            headers: [{
                label: "用户"
            }, {
                label: "所有者"
            }, {
                label: "余额"
            }, {
                label: "存入"
            }, {
                label: "取出"
            }],
            names: ["user", "owner", "balance", "deposit", "withdraw"]
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