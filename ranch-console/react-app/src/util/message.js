window.message = function (message, key) {
    var msg = message[key];
    if (!msg)
        return key;

    if (typeof (msg) === "string")
        return msg;

    var local = "zh_CN".toLowerCase().split("_");
    if (local.length === 1)
        return msg[local[0]];

    return msg[local[1]] ? msg[local[1]] : msg[local[0]];
};