import message from "./message.json";

window.meta.put("ranch.user.query", {
    page: "grid",
    message: message,
    headers: [{
        label: "身份证"
    }, {
        label: "姓名"
    }, {
        label: "昵称"
    }, {
        label: "user.email"
    }, {
        label: "user.mobile"
    }],
    names: ["idcard", "name", "nick", "email", "mobile"]
});