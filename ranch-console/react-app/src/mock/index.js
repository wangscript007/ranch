import menu from "./menu";
import user from "./user";
import account from "./account";

class Mock {
    constructor() {
        this.map = {};
    }

    enable() {
        return false;
    }

    put(uri, service, object) {
        this.map[uri + service] = object;
    }

    get(uri, params, headers) {
        var key = uri + (headers && headers.hasOwnProperty("service") ? headers.service : "");
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
account.mock(mock);

export default mock;