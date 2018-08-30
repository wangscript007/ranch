import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { LocaleProvider } from 'antd';
import zh_CN from 'antd/lib/locale-provider/zh_CN';
import { service, User } from './service';
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
            user: {
            }
        };
        this.sign = this.sign.bind(this);

        service.post('/user/sign').then(data => {
            if (data === null) {
                return;
            }

            this.setState({ user: data });
        });
    }

    public render(): JSX.Element {
        return (
            <LocaleProvider locale={zh_CN}>
                {this.state.user.id ? <Console user={this.state.user} /> : <SignIn sign={this.sign} />}
            </LocaleProvider>
        );
    }

    private sign(user: User): void {
        this.setState({ user: user });
    }
}

ReactDOM.render(
    <Index />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();

