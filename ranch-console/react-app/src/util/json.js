class Json {
    get(object, name) {
        if (typeof (object) !== "object")
            return object;

        var indexOf = name.indexOf(".");
        if (indexOf === -1)
            return object.hasOwnProperty(name) ? object[name] : "";

        var key = name.substring(0, indexOf);

        return object.hasOwnProperty(key) ? this.get(object[key], name.substring(indexOf + 1)) : JSON.stringify(object);
    }
}

window.json = new Json();