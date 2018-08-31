import storage from './storage';
import generator from './generator';

enum Type {
    Get = 'GET',
    Post = 'POST'
}

interface Json {
    code: number;
    data?: any;
    message?: string;
}

class Http {
    public post(uri: string, header: object = {}, parameter: object = {}): Promise<Json> {
        return this.json(Type.Post, uri, header, parameter);
    }

    private json(type: Type, uri: string, header: object = {}, parameter: object = {}): Promise<Json> {
        let tephraSessionId = storage.data('tephra-session-id');
        if (!tephraSessionId) {
            storage.data(tephraSessionId = storage.data('tephra-session-id', generator.random(64)));
        }

        const headers: Headers = new Headers();
        headers.append('Content-Type', 'appliaction/json');
        headers.append('tephra-session-id', tephraSessionId);
        for (const key in header) {
            if (header.hasOwnProperty(key)) {
                headers.append(key, header[key]);
            }
        }

        return fetch(this.url(uri), {
            method: type,
            mode: 'cors',
            headers: headers,
            body: JSON.stringify(parameter)
        }).then(res => res.json());
    }

    public url(uri: string): string {
        return location.port === '3000' ? (location.protocol + "//" + location.hostname + ":8080" + uri) : uri;
    }
}

export default new Http();