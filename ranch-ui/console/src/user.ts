import storage from './util/storage';
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

    public out(): void {
        service.post({ uri: '/user/sign-out' }).then(data => {
            storage.remove('tephra-session-id');
            location.reload();
        });
    }
}

export const user: UserHelper = new UserHelper();