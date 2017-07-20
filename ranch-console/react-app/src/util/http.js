import mock from "../mock";

window.ajax = function (uri, params, headers) {
    if (mock.enable())
        return mock.get(uri, params, headers);

    var object = {
        method: "POST",
        headers: {
            "Content-Type": "appliaction/json"
        }
    };
    if (params)
        object.body = JSON.stringify(params);
    if (headers)
        for (var key in headers)
            object.headers[key] = headers[key];

    return fetch(uri, object).then(res => res.json());
};