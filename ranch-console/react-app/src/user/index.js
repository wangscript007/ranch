import message from "./message.json";

window.meta.put("ranch.user.query", {
    page: "grid",
    message: message,
    search: [],
    cols: [{
        label: "身份证",
        name: "idcard"
    }, {
        label: "姓名",
        name: "name"
    }, {
        label: "昵称",
        name: "nick"
    }, {
        label: "user.email",
        name: "email"
    }, {
        label: "user.mobile",
        name: "mobile"
    }]
});