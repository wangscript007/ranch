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
        });
    }

    render(): JSX.Element {
        document.title = storage.title();
        let image: Image = new Image({
            name: 'portrait',
            fieldName: 'ranch.user.portrait',
            defaultValue: this.state.user.portrait || 'img/logo.png'
        });

        return (
            <div id="ranch-ui-mobile">
                <Top>{storage.title()}</Top>
                <div className="layout-content">
                    <div className="mine-area">
                        <div className="portrait">{image.render()}</div>
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
