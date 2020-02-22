import {
    service
} from "../http";

class Meta {
    constructor() {
        this.map = {};
    }

    get = uri => {
        let key = uri.substring(0, uri.lastIndexOf('/') + 1);
        if (key in this.map) {
            return new Promise((resolve, reject) => {
                resolve(this.map[key]);
            });
        }

        return service('/ui/console/meta', {
            domain: 'console',
            key: key
        }).then(data => {
            if (data != null) {
                this.map[key] = data;
            }

            return data;
        });
    }

    props = (full, sub) => {
        if (!full) return sub;

        if (!sub) return full;

        let ps = [];
        for (let s of sub) {
            for (let f of full) {
                if (f.name === s.name) {
                    ps.push({
                        ...f,
                        ...s
                    });

                    break;
                }
            }
        }

        return ps;
    }
}

const meta = new Meta();

export default meta;