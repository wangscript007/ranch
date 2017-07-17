window.ajax = function (uri, params, headers) {
    var object = {
        method: "POST",
        headers: {
            // "Content-Type": "appliaction/json"
        }
    };
    if (params)
        object.body = JSON.stringify(params);
    if (headers)
        for (var key in headers)
            object.headers[key] = headers[key];

    return fetch("http://127.0.0.1:8080" + uri, object).then(res => res.json());
};