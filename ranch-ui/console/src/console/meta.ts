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
    readonly?: boolean;
    remote?: {
        service: string;
        header?: object;
        parameter?: object;
        value?: string;
        label: string[];
    }
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
    private service: string;

    public get(key: string): Promise<Meta> {
        const uri = key.charAt(0) === '/';
        const indexOf: number = uri ? (key.lastIndexOf('/') + 1) : key.lastIndexOf('.');
        this.key = key.substring(0, indexOf);
        this.service = key.substring(uri ? indexOf : (indexOf + 1));
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

    public nowKey(): string {
        return this.key;
    }

    public nowService(): string {
        return this.service;
    }

    public now(): Meta {
        return this.map[this.key];
    }
}

export const meta: MetaHelper = new MetaHelper();