import http from '../util/http';
import note from '../util/note';

export interface User {
    id?: string;
    name?: string;
    nick?: string;
    mobile?: string;
    portrait?: string;
}

export interface Classify {
    code?: string;
    key?: string;
    name?: string;
    value?: string;
}

class Service {
    public signIn(from: string): Promise<User | null> {
        return this.sign().then(user => {
            if (user === null)
                location.href = 'mobile-sign-in.html?' + from;

            return user;
        });
    }

    public sign(): Promise<User | null> {
        return service.post('/user/sign').then(user => {
            return user && user.id ? user : null;
        });
    }

    public classify(code: string, key: string): Promise<Classify> {
        return service.post('/classify/find', {}, { code: code, key: key });
    }

    public forwardToClassify(from?: string, code?: string, key?: string): void {
        if (!from || !code || !key)
            return;

        location.href = 'classify.html?' + from + ',' + code + ',' + key;
    }

    public back(): void {
        let args = this.getArgsFromLocation();
        location.href = (args.length === 0 ? 'index' : args[0]) + '.html';
    }

    public getArgsFromLocation(): string[] {
        let href = location.href;
        let indexOf = href.indexOf('?');
        if (indexOf === -1)
            return [];

        let parameter = href.substring(indexOf + 1);
        if (parameter.indexOf(',') === -1)
            return [parameter];

        return parameter.split(',');
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