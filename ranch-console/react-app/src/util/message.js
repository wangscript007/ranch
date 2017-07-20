window.message = function (message, key) {
    if (!key)
        return "";

    if (!message || !message.hasOwnProperty(key))
        return key;

    var msg = message[key];
    if (typeof (msg) !== "object")
        return msg;

    var lang = (navigator.language || navigator.userLanguage || "zh-cn").toLowerCase().split("-");
    for (var i = lang.length - 1; i > -1; i--)
        if (msg.hasOwnProperty(lang[i]))
            return msg[lang[i]];

    for (var k in msg)
        return msg[k];

    return key;
};