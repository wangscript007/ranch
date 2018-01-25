import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../registerServiceWorker';
import storage from '../util/storage';
import message from '../util/message';
import { service } from './service';
import { Top } from './top';
import { Bottom } from './bottom';
import './mine.less';

class Mine extends React.Component<object, object> {
    constructor(props: object) {
        super(props);

        service.signIn('mine');
    }

    render(): JSX.Element {
        document.title = storage.title();

        return (
            <div id="ranch-ui-mobile">
                <Top>{storage.title()}</Top>
                <div className="layout-content">
                    <div className="mine-area">
                        <div className="portrait"><img src="" /></div>
                        <div className="nick-mobile">
                            <div className="nick">{service.getUser().nick || ''}</div>
                            <div className="mobile">{service.getUser().mobile || ''}</div>
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
