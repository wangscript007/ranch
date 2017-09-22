import mock from "../mock";

window.ajax = function (uri, params, headers) {
    if (mock.enable())
        return mock.get(uri, params, headers);

    var object = {
        method: "POST",
        headers: headers || {}
    };
    object.headers["Content-Type"] = "appliaction/json";
    if (params)
        object.body = JSON.stringify(params);

    return fetch("http://dev3.nutsb.com" + uri, object).then(res => res.json());
};