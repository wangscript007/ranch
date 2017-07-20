class Bean {
    constructor() {
        this.map = {};
    }

    put(key, object) {
        this.map[key] = object;
    }

    get(key) {
        return this.map[key];
    }
}

window.bean = new Bean();