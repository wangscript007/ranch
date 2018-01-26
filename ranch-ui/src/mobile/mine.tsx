import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../registerServiceWorker';
import storage from '../util/storage';
import message from '../util/message';
import Image from '../ui/image';
import { service, User } from './service';
import { Top } from './top';
import { Bottom } from './bottom';
import './mine.less';

interface State {
    user: User;
}

class Mine extends React.Component<object, State> {
    private portrait: Image | null;

    constructor(props: object) {
        super(props);

        this.state = {
            user: {}
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
        });
    }

    render(): JSX.Element {
        document.title = storage.title();

        return (
            <div id="ranch-ui-mobile">
                <Top>{storage.title()}</Top>
                <div className="layout-content">
                    <div className="mine-area">
                        <div className="portrait">
                            <Image name="portrait" ref={image => this.portrait = image} fieldName="ranch.user.portrait"
                                defaultValue={this.state.user.portrait || 'img/logo.png'} />
                        </div>
                        <div className="nick-mobile">
                            <div className="nick">{this.state.user.nick || message.get('empty')}</div>
                            <div className="mobile">{this.state.user.mobile || message.get('empty')}</div>
                        </div>
                    </div>
                    <div className="mine-area">
                        <div onClick={() => this.signOut()}>{message.get('sign-out')}</div>
                    </div>
                </div>
                <Bottom active={2} />
            </div>
        );
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
