class Meta {
    constructor() {
        this.map = {};
    }

    load(key) {
        var k = this.prefix(key, 0);
        console.log(this.map[k]);
        if (this.map[k])
            return new Promise((resolve, reject) => resolve(this.map[k]));

        return window.ajax("/console/meta", {}, {
            key: k
        }).then(json => {
            if (json.hasOwnProperty("code") && json.code === 0)
                this.map[k] = json.data;

            return this.map[k];
        });
    }

    put(key, meta) {
        console.log(meta);
        this.map[key] = meta;
    }

    get(key) {
        return this.map[this.prefix(key, 0)] || {};
    }

    props(key) {
        return this.get(key).props || [];
    }

    service(key) {
        return this.get(key)[this.suffix(key)] || {};
    }

    page(key) {
        return this.service(key).page;
    }

    ops(key) {
        return this.buttons(key, "ops");
    }

    toolbar(key) {
        return this.buttons(key, "toolbar");
    }

    buttons(key, name) {
        var buttons = this.service(key)[name];
        if (!buttons || buttons.length === 0)
            return buttons;

        var prefix = this.prefix(key, 1);
        for (var i = 0; i < buttons.length; i++) {
            this.addPrefix(buttons[i], "service", prefix);
            this.addPrefix(buttons[i], "success", prefix);
        }

        return buttons;
    }

    addPrefix(button, name, prefix) {
        var value = button[name];
        if (value && value.indexOf(".") === -1)
            button[name] = prefix + value;
    }

    prefix(key, offset) {
        var lastIndexOf = key.lastIndexOf(".");

        return lastIndexOf === -1 ? key : key.substring(0, lastIndexOf + offset);
    }

    suffix(key) {
        var lastIndexOf = key.lastIndexOf(".");

        return lastIndexOf === -1 ? key : key.substring(lastIndexOf + 1);
    }
}

window.meta = new Meta();