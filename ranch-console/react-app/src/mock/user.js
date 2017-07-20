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
                email: "e" + i + "@mail ",
                mobile: "1231234567" + i
            };
        }
        mock.put("/console/service", "ranch.user.query", list);
    }
}

export default new User();