import { message } from 'antd';
import http from './util/http';

export interface User {
    id?: string;
    nick?: string;
    portrait?: string;
}

class Service {
    public post(uri: string, header: object = {}, parameter: object = {}): Promise<any> {
        message.config({
            maxCount: 1
        });
        const loading = message.loading('loading');
        return http.post(uri, header, parameter).then(json => {
            loading();
            if (json.code !== 0) {
                message.error('[' + json.code + ']' + json.message);

                return null;
            }

            return json.data;
        });
    }
}

export const service = new Service();