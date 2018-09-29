import { message } from 'antd';
import http from './util/http';
import merger from './util/merger';

export interface Request {
    uri: string;
    header?: {};
    parameter?: {};
}

class Service {
    public post(request: Request): Promise<any> {
        message.config({
            maxCount: 1
        });
        const loading = message.loading('loading');
        const domain: string = location.pathname.replace(/\//g, '');
        return http.post(request.uri, merger.merge({}, request.header || {}, { domain: domain === '' ? 'console' : domain }), request.parameter || {})
            .then(json => {
                loading();
                if (json.code !== 0) {
                    message.error('[' + json.code + ']' + json.message);
                    if (json.code === 9901) {
                        location.reload();
                    }

                    return null;
                }

                return json.data;
            });
    }
}

export const service: Service = new Service();