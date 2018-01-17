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

export interface Success {
    service: string;
    parameter?: object;
}

class Service {
    private metas: Meta[] = [];
    private content: React.Component;
    private parameter: object | null;

    public sign(): Promise<User> {
        return service.post('/user/sign');
    }

    public menu(): Promise<Menu[]> {
        return this.post('/console/menu');
    }

    public bind(content: React.Component): void {
        this.content = content;
    }

    public setParameter(parameter: object | null): void {
        this.parameter = parameter;
    }

    public getParameter(parameter?: object): object {
        if (!parameter || !this.parameter)
            return {};

        let param: object = {};
        for (const key in this.parameter)
            if (parameter.hasOwnProperty(key))
                param[key] = this.parameter[key];

        return param;
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

    public execute(key: string, header: object = {}, parameter: object = {}, success?: Success): Promise<any> {
        header['key'] = key;

        let promise = this.post('/console/service', header, parameter);
        if (!success || !success.service)
            return promise;

        return promise.then(json => {
            if (json != null)
                this.to(success.service, success.parameter);

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