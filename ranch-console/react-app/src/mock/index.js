import menu from "./menu";
import user from "./user";

class Mock {
    constructor() {
        this.map = {};
    }

    enable() {
        return true;
    }

    put(key, object) {
        this.map[key] = object;
    }

    get(uri, params) {
        var map = this.map;
        var key = params && params.hasOwnProperty("service") ? params.service : uri;
        return new Promise(function (resolve, reject) {
            resolve({
                code: 0,
                data: map.hasOwnProperty(key) ? map[key] : {}
            });
        });
    }
}

var mock = new Mock();
menu.mock(mock);
user.mock(mock);

export default mock;