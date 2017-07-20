window.message = function (message, key) {
    if (!key)
        return "";

    if (!message || !message.hasOwnProperty(key))
        return key;

    var msg = message[key];
    if (typeof (msg) !== "object")
        return msg;

    var local = "zh_CN".toLowerCase().split("_");
    for (var i = local.length - 1; i > -1; i--)
        if (msg.hasOwnProperty(local[i]))
            return msg[local[i]];

    for (var k in msg)
        return msg[k];

    return key;
};