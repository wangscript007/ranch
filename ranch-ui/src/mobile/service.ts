import http from '../util/http';
import note from '../util/note';
import message from '../util/message';

export interface User {
    id?: string;
    nick?: string;
    mobile?: string;
}

class Service {
    private user: User;

    public getUser(): User {
        return this.user || {
            nick: message.get('nick.non-sign-in'),
            mobile: '12312345678'
        };
    }

    public signIn(from: string): void {
        this.sign().then(user => {
            if (!user || !user.id)
                location.href = 'mobile-sign-in.html?' + from;
        });
    }

    public sign(): Promise<User> {
        return service.post('/user/sign').then(user => {
            if (user && user.id)
                this.user = user;

            return user;
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