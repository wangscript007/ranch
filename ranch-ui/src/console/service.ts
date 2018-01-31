import http from '../util/http';
import note from '../util/note';
import { Meta } from './meta';

export interface User {
    id?: string;
    nick?: string;
}

export interface Menu {
    label: string;
    items: MenuItem[];
}

export interface MenuItem {
    label: string;
    service: string;
    parameter?: object;
}

export interface Success {
    service: string;
    parameter?: object;
}

class Service {
    private metas: Meta[] = [];
    private content: React.Component;
    private parameter?: object;

    public sign(): Promise<User> {
        return service.post('/user/sign');
    }

    public menu(): Promise<Menu[]> {
        return this.post('/console/menu');
    }

    public bind(content: React.Component): void {
        this.content = content;
    }

    public setParameter(parameter?: object): void {
        this.parameter = parameter;
    }

    public getParameter(parameter?: object): object {
        if (!parameter || !this.parameter)
            return parameter || this.parameter || {};

        for (const key in this.parameter)
            parameter[key] = this.parameter[key];

        return parameter;
    }

    public to(service: string, parameter?: object, data?: object): void {
        let indexOf = service.lastIndexOf('.');
        this.meta(service.substring(0, indexOf)).then(meta => this.content.setState({
            page: meta[service.substring(indexOf + 1)].type,
            service: service,
            parameter: this.getParameter(parameter),
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
            if (json !== null)
                this.to(success.service, success.parameter);

            return null;
        });
    }

    public post(uri: string, header: object = {}, parameter: object = {}): Promise<any> {
        return http.post(uri, header, parameter).then(json => {
            if (json.code === 0)
                return json.data;

            note.show(json.code, json.message || '', 10 * 1000);

            return null;
        });
    }
}

export const service = new Service();