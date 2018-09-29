import { service } from './service';

export interface User {
    id?: string;
    nick?: string;
    portrait?: string;
}

class UserHelper {
    private user: User;

    public sign(): Promise<User> {
        return service.post({ uri: '/user/sign' }).then(data => this.user = data);
    }

    public get(): User {
        return this.user;
    }

    public set(user: User): void {
        this.user = user;
    }
}

export const user: UserHelper = new UserHelper();