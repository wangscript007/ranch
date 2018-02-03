import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../../registerServiceWorker';
import message from '../../util/message';
import selector from '../../util/selector';
import Icon from '../../ui/icon';
import { service } from '../service';
import Panel from '../panel';
import './i18n';
import './index.less';

interface State {
    up?: boolean;
}

class SignIn extends Panel<object, State> {
    constructor(props: object) {
        super(props);

        this.state = {};
    }

    protected getTitle(): string {
        return 'sign-in-up';
    }

    protected getContent(): JSX.Element | JSX.Element[] | null {
        return [
            <div className='sign-in'>
                <div className="label"><Icon code="&#xe603;" /> {message.get('sign-in.mobile')}</div>
                <div><input type="text" id="uid" name="uid" placeholder={message.get('sign-in.mobile.placeholder')} /></div>
                <div className="label"><Icon code="&#xe601;" /> {message.get('sign-in.password')}</div>
                <div><input type="password" id="password" name="password" placeholder={message.get('sign-in.password.placeholder')} /></div>
                <div className="toolbar">{this.toolbar()}</div>
            </div>,
            <div className="sign-in-third">
                <div className="title"><span>{message.get('sign-in-third')}</span></div>
                <div className="icons">
                    <Icon code="&#xe600;" />
                </div>
            </div>
        ];
    }

    private toolbar(): JSX.Element[] {
        if (this.state.up) {
            return [
                <button onClick={() => this.sign('up')}>{message.get('sign-up')}</button>,
                <span onClick={() => this.setState({ up: false })}>{message.get('sign-in')}</span>
            ];
        }

        return [
            <button onClick={() => this.sign('in')}>{message.get('sign-in')}</button>,
            <span onClick={() => this.setState({ up: true })}>{message.get('sign-up')}</span>
        ];
    }

    private sign(type: string): void {
        service.post('/user/sign-' + type, {}, {
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
