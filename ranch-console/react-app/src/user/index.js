import message from "./message.json";

window.meta.put("ranch.user.query", {
    page: "grid",
    message: message,
    search: [],
    cols: [{
        label: "身份证",
        name: "idcard",
        search: "hello"
    }, {
        label: "姓名",
        name: "name",
        search: "input",
        placeholder:"user.name.placeholder"
    }, {
        label: "昵称",
        name: "nick"
    }, {
        label: "user.email",
        name: "email"
    }, {
        label: "user.mobile",
        name: "mobile"
    }, {
        label: "user.gender",
        name: "gender",
        select: [{
            value: -1,
            label: "全部"
        }, {
            value: 0,
            label: "未知"
        }, {
            value: 1,
            label: "男"
        }, {
            value: 2,
            label: "user.gender.female"
        }],
        search: "select"
    }]
});