import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../registerServiceWorker';
import message from '../util/message';
import selector from '../util/selector';
import Icon from '../ui/icon';
import { service } from './service';
import './i18n';
import './sign-in.less';

class SignIn extends React.Component {
    render(): JSX.Element {
        return (
            <div id="ranch-ui-sign-in">
                <div className="label"><Icon code="&#xe603;" /> {message.get('sign-in.mobile')}</div>
                <div><input type="text" id="uid" name="uid" placeholder={message.get('sign-in.mobile.placeholder')} /></div>
                <div className="label"><Icon code="&#xe601;" /> {message.get('sign-in.password')}</div>
                <div><input type="password" id="password" name="password" placeholder={message.get('sign-in.password.placeholder')} /></div>
                <div className="button"><button onClick={() => this.signIn()}>{message.get('sign-in')}</button></div>
            </div>
        );
    }

    private signIn(): void {
        service.post('/user/sign-in', {}, {
            uid: selector.value('#uid'),
            password: selector.value('#password'),
            type: 1
        }).then(user => {
            if (!user || !user.hasOwnProperty('id'))
                return;

            let href = location.href;
            let indexOf = href.lastIndexOf('?');
            location.href = (indexOf > -1 ? href.substring(indexOf + 1) : 'index') + '.html';
        });
    }
}

ReactDOM.render(
    <SignIn />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
