class Meta {
    constructor() {
        this.map = {};
    }

    put(key, meta) {
        this.map[key] = meta;
    }

    get(key) {
        return this.map[key];
    }
}

window.meta = new Meta();