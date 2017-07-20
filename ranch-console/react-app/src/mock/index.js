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

    get(uri, params, headers) {
        var key = headers && headers.hasOwnProperty("service") ? headers.service : uri;
        var data = this.map.hasOwnProperty(key) ? this.map[key] : {};
        return new Promise(function (resolve, reject) {
            resolve({
                code: 0,
                data: data
            });
        });
    }
}

var mock = new Mock();
menu.mock(mock);
user.mock(mock);

export default mock;