import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../../registerServiceWorker';
import message from '../../util/message';
import selector from '../../util/selector';
import { service, User } from '../service';
import Panel from '../panel';
import './i18n';
import './index.less';

interface State {
    user?: User;
}

class Modify extends Panel<object, State> {
    constructor(props: object) {
        super(props);

        this.state = {};

        service.signIn('mine').then(user => {
            if (user === null)
                return;

            this.setState({
                user: user
            });
        });
    }

    protected back(): void {
        location.href = 'mine.html';
    }

    protected getTitle(): string {
        return 'sign.modify';
    }

    protected getContent(): JSX.Element | JSX.Element[] | null {
        if (!this.state.user)
            return null;

        return (
            <div className="sign-modify">
                <label htmlFor="sign-name">{message.get('sign.name')}</label>
                <input type="text" id="sign-name" name="name" defaultValue={this.state.user.name || ''} />
                <label htmlFor="sign-nick">{message.get('sign.nick')}</label>
                <input type="text" id="sign-nick" name="nick" defaultValue={this.state.user.nick || ''} />
                <div className="toolbar"><button onClick={() => this.submit()}>{message.get('sign.modify')}</button></div>
            </div>
        );
    }

    private submit(): void {
        service.post('/user/modify', {}, {
            name: selector.value('#sign-name'),
            nick: selector.value('#sign-nick')
        }).then(data => {
            if (data === null)
                return;

            this.back();
        });
    }
}

ReactDOM.render(
    <Modify />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
