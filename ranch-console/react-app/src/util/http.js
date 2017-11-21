import mock from "../mock";

window.ajax = function (uri, params, headers) {
    if (mock.enable())
        return mock.get(uri, params, headers);

    var object = {
        method: "POST",
        mode: "cors",
        credentials: "include",
        headers: headers || {}
    };
    object.headers["Content-Type"] = "appliaction/json";
    if (params)
        object.body = JSON.stringify(params);

    return fetch("http://localhost:8080" + uri, object).then(res => res.json());
};