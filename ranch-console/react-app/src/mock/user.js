class User {
    mock(mock) {
        this.query(mock);
    }

    query(mock) {
        var list = [];
        for (var i = 0; i < 10; i++) {
            list[i] = {
                id: "id " + i,
                idcard: "1234567890" + i,
                name: "姓名 " + i,
                nick: "昵称 " + i,
                email: "Email " + i,
                mobile: "1231234567" + i
            };
        }
        mock.put("ranch.user.query", {
            list: list
        });
    }
}

export default new User();