import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../../registerServiceWorker';
import message from '../../util/message';
import selector from '../../util/selector';
import { service } from '../service';
import Panel from '../panel';
import './i18n';
import './index.less';

class Password extends Panel {
    protected back(): void {
        location.href = 'mine.html';
    }

    protected getTitle(): string {
        return 'sign.password';
    }

    protected getContent(): JSX.Element | JSX.Element[] | null {
        return (
            <div className="sign-modify">
                <label htmlFor="sign-password-old">{message.get('sign.password.old')}</label>
                <input type="password" id="sign-password-old" />
                <label htmlFor="sign-password-new">{message.get('sign.password.new')}</label>
                <input type="password" id="sign-password-new" />
                <label htmlFor="sign-password-repeat">{message.get('sign.password.repeat')}</label>
                <input type="password" id="sign-password-repeat" />
                <div className="toolbar"><button onClick={() => this.submit()}>{message.get('sign.password')}</button></div>
            </div>
        );
    }

    private submit(): void {
        service.post('/user/password', {}, {
            old: selector.value('#sign-password-old'),
            'new': selector.value('#sign-password-new'),
            repeat: selector.value('#sign-password-repeat')
        }).then(data => {
            if (data === null)
                return;

            this.setState({
                password: false
            });
        });

    }
}

ReactDOM.render(
    <Password />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
