import { service } from '../service';

export interface Meta {
    key?: string;
    uri?: string;
    props: PropMeta[];
}

export interface PropMeta {
    name: string;
    label?: string;
    type?: string;
    labels?: string[];
    values?: object;
    upload?: string;
    ignore?: string[];
}

export interface PageMeta {
    type: string;
    service?: string;
    parameter?: object;
    search?: PropMeta[];
    toolbar?: ActionMeta[];
    ops?: ActionMeta[];
}

export interface ActionMeta {
    type: string;
    service?: string;
    icon?: string;
    label?: string;
    when?: string;
    parameter?: {};
    success?: {
        service: string,
        header?: {},
        parameter?: {}
    };
}

class MetaHelper {
    private map: { [key: string]: Meta } = {};
    private key: string;

    public get(key: string): Promise<Meta> {
        this.key = this.getKey(key);
        if (this.map.hasOwnProperty(this.key)) {
            return new Promise((resolve, reject) => resolve(this.now()));
        }

        return service.post({
            uri: '/ui/console/meta',
            header: {
                key: this.key
            }
        }).then(data => {
            if (data === null) {
                return null;
            }

            if (data.hasOwnProperty('key')) {
                this.map[data.key] = data;
            }
            if (data.hasOwnProperty('uri')) {
                this.map[data.uri] = data;
            }

            return data;
        });
    }

    private getKey(key: string): string {
        if (key.charAt(0) === '/') {
            return key.substring(0, key.lastIndexOf('/') + 1);
        }

        return key.substring(0, key.lastIndexOf('.'));
    }

    public now(): Meta {
        console.log(this.key);
        console.log(this.map[this.key]);
        return this.map[this.key];
    }
}

export const meta: MetaHelper = new MetaHelper();