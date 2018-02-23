import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../registerServiceWorker';
import selector from '../util/selector';
import message from '../util/message';
import { service } from './service';
import Icon from '../ui/icon';
import './i18n';
import './sign-in.less';

class SignIn extends React.Component {
    constructor(props: object) {
        super(props);

        service.sign().then(user => {
            if (user && user.id)
                location.href = 'console.html';
        });
    }
    render(): JSX.Element {
        return (
            <div id="ranch-ui-sign-in">
                <div className="label"><Icon code="&#xe603;" /> {message.get('sign-in.username')}</div>
                <div><input type="text" id="uid" placeholder={message.get('sign-in.username.placeholder')} autoFocus={true} /></div>
                <div className="label"><Icon code="&#xe601;" /> {message.get('sign-in.password')}</div>
                <div><input type="password" id="password" placeholder={message.get('sign-in.password.placeholder')} onKeyDown={(event) => this.keyDown(event)} /></div>
                <div className="button"><button onClick={() => this.signIn()}>{message.get('sign-in')}</button></div>
            </div>
        );
    }

    private keyDown(event: React.KeyboardEvent<any>): void {
        if (event.key === 'Enter')
            this.signIn();
    }

    private signIn(): void {
        service.post('/user/sign-in', {}, {
            uid: selector.value('#uid'),
            password: selector.value('#password'),
            type: 1
        }).then(user => {
            if (!user || !user.hasOwnProperty('id'))
                return;

            location.href = 'console.html';
        });
    }
}

ReactDOM.render(
    <SignIn />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
