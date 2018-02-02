import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../../registerServiceWorker';
import message from '../../util/message';
import selector from '../../util/selector';
import Image from '../../ui/image';
import { service, User, Classify } from '../service';
import Page from '../page';
import './i18n';
import './index.less';

interface State {
    user: User;
    aboutUs: Classify;
    contactUs: Classify;
    modify?: boolean;
    password?: boolean;
}

class Mine extends Page<object, State> {
    private portrait: Image | null;

    constructor(props: object) {
        super(props);

        this.state = {
            user: {},
            aboutUs: {},
            contactUs: {}
        };

        service.signIn('mine').then(user => {
            if (user === null)
                return;

            this.setState({
                user: user
            });
            if (this.portrait) {
                this.portrait.setState({
                    path: user.portrait || 'img/logo.png'
                });
            }
            service.classify('setting.doc', 'about-us').then(classify => this.setState({ aboutUs: classify }));
            service.classify('setting.doc', 'contact-us').then(classify => this.setState({ contactUs: classify }));
        });
    }

    protected getTitle(): string {
        return 'mine';
    }

    protected getContent(): JSX.Element | JSX.Element[] | null {
        return [
            <div className="mine-area">
                <div className="portrait">
                    <Image name="portrait" ref={image => this.portrait = image} fieldName="ranch.user.portrait"
                        defaultValue={this.state.user.portrait || 'img/logo.png'} />
                </div>
                <div className="nick-mobile">
                    <div className="nick">{this.state.user.nick || message.get('empty')}</div>
                    <div className="mobile">
                        {this.state.user.mobile ? this.state.user.mobile.replace(/(\d{3})\d{4}(\d{4})/, '$1****$2') : message.get('empty')}
                    </div>
                </div>
            </div>,
            <div className="mine-area">
                <div onClick={() => this.setState(prevState => ({ modify: !prevState.modify }))}>{message.get('sign.modify')}</div>
                {this.modify()}
                <div className="line" />
                <div onClick={() => this.setState(prevState => ({ password: !prevState.password }))}>{message.get('sign.modify.password')}</div>
                {this.password()}
            </div>,
            <div className="mine-area">
                <div>{this.state.aboutUs.name || message.get('about-us')}</div>
                <div className="line" />
                <div>{this.state.contactUs.name || message.get('contact-us')}</div>
            </div>,
            <div className="mine-area">
                <div onClick={() => this.signOut()}>{message.get('sign-out')}</div>
            </div>
        ];
    }

    protected getBottom(): number {
        return 2;
    }

    private modify(): JSX.Element | null {
        if (!this.state.modify)
            return null;

        return (
            <div className="sign-modify">
                <label htmlFor="sign-name">{message.get('sign.name')}</label>
                <input type="text" id="sign-name" name="name" defaultValue={this.state.user.name || ''} />
                <label htmlFor="sign-nick">{message.get('sign.nick')}</label>
                <input type="text" id="sign-nick" name="nick" defaultValue={this.state.user.nick || ''} />
                <div className="submit"><button onClick={() => this.modifySubmit()}>{message.get('sign.modify')}</button></div>
            </div>
        );
    }

    private modifySubmit(): void {
        service.post('/user/modify', {}, {
            name: selector.value('#sign-name'),
            nick: selector.value('#sign-nick')
        }).then(data => {
            if (data === null)
                return;

            this.setState({
                user: data,
                modify: false
            });
        });
    }

    private password(): JSX.Element | null {
        if (!this.state.password)
            return null;

        return (
            <div className="sign-modify">
                <label htmlFor="sign-password-old">{message.get('sign.password.old')}</label>
                <input type="password" id="sign-password-old" />
                <label htmlFor="sign-password-new">{message.get('sign.password.new')}</label>
                <input type="password" id="sign-password-new" />
                <label htmlFor="sign-password-repeat">{message.get('sign.password.repeat')}</label>
                <input type="password" id="sign-password-repeat" />
                <div className="submit"><button onClick={() => this.passwordSubmit()}>{message.get('sign.modify.password')}</button></div>
            </div>
        );
    }

    private passwordSubmit(): void {
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

    private signOut(): void {
        service.post('/user/sign-out').then(data => {
            location.href = 'index.html';
        });
    }
}

ReactDOM.render(
    <Mine />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
