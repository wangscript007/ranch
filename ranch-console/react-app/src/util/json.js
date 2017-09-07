class Json {
    get(object, name) {
        if (typeof (object) !== "object")
            return object;

        if (object.hasOwnProperty("length")) {
            if (object.length === 0)
                return "";

            var string = "";
            for (var i = 0; i < object.length; i++)
                string = "," + this.getFromObject(object[i], name);

            return string.substring(1);
        }


        return this.getFromObject(object, name);
    }

    getFromObject(object, name) {
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