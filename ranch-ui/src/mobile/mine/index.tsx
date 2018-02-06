import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../../registerServiceWorker';
import message from '../../util/message';
import Image from '../../ui/image';
import Icon from '../../ui/icon';
import { service, User, Classify } from '../service';
import Page from '../page';
import './i18n';
import './index.less';

interface State {
    user: User;
    aboutUs: Classify;
    contactUs: Classify;
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
                <div className="forward-to" onClick={() => location.href = 'sign-modify.html'}>
                    <Icon code="&#xe60f;" />{message.get('sign.modify')}
                </div>
                <div className="line" />
                <div className="forward-to" onClick={() => location.href = 'sign-password.html'}>
                    <Icon code="&#xe60f;" />{message.get('sign.password')}
                </div>
            </div>,
            <div className="mine-area">
                <div className="forward-to" onClick={() => service.forwardToClassify('mine', this.state.aboutUs.code, this.state.aboutUs.key)}>
                    <Icon code="&#xe60f;" />{this.state.aboutUs.name || message.get('about-us')}
                </div>
                <div className="line" />
                <div className="forward-to" onClick={() => service.forwardToClassify('mine', this.state.contactUs.code, this.state.contactUs.key)}>
                    <Icon code="&#xe60f;" />{this.state.contactUs.name || message.get('contact-us')}
                </div>
            </div>,
            <div className="mine-area">
                <div onClick={() => this.signOut()}>{message.get('sign-out')}</div>
            </div>
        ];
    }

    protected getBottom(): number {
        return 3;
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
