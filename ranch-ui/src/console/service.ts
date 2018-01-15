import http from '../util/http';
import { Meta } from './meta';

export interface User {
    id?: string;
    nick?: string;
}

export interface Menu {
    name: string;
    items: {
        name: string;
        service: string;
    }[];
}

class Service {
    private metas: Meta[] = [];
    private content: React.Component;

    public sign(): Promise<User> {
        return service.post('/user/sign');
    }

    public menu(): Promise<Menu[]> {
        return this.post('/console/menu');
    }

    public bind(content: React.Component): void {
        this.content = content;
    }

    public to(service: string, parameter?: object, data?: object): void {
        let indexOf = service.lastIndexOf('.');
        this.meta(service.substring(0, indexOf)).then(meta => this.content.setState({
            page: meta[service.substring(indexOf + 1)].type,
            service: service,
            parameter: parameter,
            meta: meta,
            data: data
        }));
    }

    private meta(key: string): Promise<Meta> {
        if (this.metas[key]) {
            return new Promise<Meta>((resolve, reject) => {
                resolve(this.metas[key]);
            });
        }

        return this.post('/console/meta', { key: key }).then(data => this.metas[key] = data);
    }

    public execute(key: string, header: object = {}, parameter: object = {}, success?: string): Promise<any> {
        header['key'] = key;

        let promise = this.post('/console/service', header, parameter);
        if (!success)
            return promise;

        return promise.then(json => {
            if (json != null)
                this.to(success);

            return null;
        });
    }

    public post(uri: string, header: object = {}, parameter: object = {}): Promise<any> {
        return http.post(uri, header, parameter).then(json => {
            if (json.code == 0)
                return json.data;

            alert('[' + json.code + ']' + json.message);

            return null;
        });
    }
}

export const service = new Service();