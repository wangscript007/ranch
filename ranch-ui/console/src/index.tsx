import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { LocaleProvider } from 'antd';
import zh_CN from 'antd/lib/locale-provider/zh_CN';
import { user, User } from './user';
import SignIn from './sign-in';
import Console from './console';
import registerServiceWorker from './registerServiceWorker';
import './index.scss';

interface State {
    user: User;
}

class Index extends React.Component<object, State> {
    public constructor(props: object) {
        super(props);

        this.state = {
            user: user.get()
        };
        this.sign = this.sign.bind(this);
    }

    public render(): JSX.Element {
        return this.state.user.id ? <Console user={this.state.user} /> : <SignIn sign={this.sign} />;
    }

    private sign(user: User): void {
        this.setState({ user: user });
    }
}

user.sign().then(data => {
    ReactDOM.render(
        <LocaleProvider locale={zh_CN}><Index /></LocaleProvider>,
        document.getElementById('root') as HTMLElement
    );
    registerServiceWorker();
});


