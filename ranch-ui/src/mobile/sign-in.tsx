import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../registerServiceWorker';
import message from '../util/message';
import Icon from '../ui/icon';
import './i18n';
import './sign-in.less';

class SignIn extends React.Component {
    render(): JSX.Element {
        return (
            <div id="ranch-ui-sign-in">
                <div className="label"><Icon code="&#xe603;" /> {message.get('sign-in.mobile')}</div>
                <div><input type="text" name="uid" placeholder={message.get('sign-in.mobile.placeholder')} /></div>
                <div className="label"><Icon code="&#xe601;" /> {message.get('sign-in.password')}</div>
                <div><input type="password" name="password" placeholder={message.get('sign-in.password.placeholder')} /></div>
                <div className="button"><button onClick={() => this.signIn()}>{message.get('sign-in')}</button></div>
            </div>
        );
    }

    private signIn():void{
    }
}

ReactDOM.render(
    <SignIn />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
