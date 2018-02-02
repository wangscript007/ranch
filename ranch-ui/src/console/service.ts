import http from '../util/http';
import selector from '../util/selector';
import merger from '../util/merger';
import note from '../util/note';
import { Meta, Page } from './meta';

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

        return merger.merge<object>({}, parameter, this.parameter);
    }

    public to(service: string, parameter?: object, data?: object): void {
        let param = this.getParameter(parameter);
        let indexOf = service.lastIndexOf('.');
        this.meta(service.substring(0, indexOf)).then(meta => {
            let page: Page = meta[service.substring(indexOf + 1)];
            if (data)
                this.setContent(service, meta, page.type, param, data);
            else {
                let serv: string = service;
                if (page.service)
                    serv = service.substring(0, indexOf + 1) + page.service;
                this.execute(serv, {}, param).then(dt => this.setContent(service, meta, page.type, param, dt));
            }
        });
    }

    private meta(key: string): Promise<Meta> {
        if (this.metas[key]) {
            return new Promise<Meta>((resolve, reject) => {
                resolve(this.metas[key]);
            });
        }

        return this.post('/console/meta', { key: key }).then(data => this.metas[key] = data);
    }

    private setContent(service: string, meta: Meta, page: string, parameter: object, data?: object): void {
        this.content.setState({ page: 'blank' }, () => {
            this.content.setState({
                page: page,
                service: service,
                parameter: parameter,
                meta: meta,
                data: data
            });
        });
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
        let loading = selector.find('#loading');
        if (loading)
            loading.style.display = '';

        return http.post(uri, header, parameter).then(json => {
            if (loading)
                loading.style.display = 'none';
            if (json.code === 0)
                return json.data;

            note.show(json.code, json.message || '', 10 * 1000);

            return null;
        });
    }


}

export const service = new Service();