import * as React from 'react';
import * as ReactDOM from 'react-dom';
import message from '../util/message';
import Accordion from '../ui/accordion';
import registerServiceWorker from '../registerServiceWorker';
import { Meta } from './meta';
import { service, User, Menu } from './service';
import { Content } from './content';
import './index.less';

interface State {
    user: User;
    menus: Menu[];
    page: string;
    service?: string;
    meta?: Meta;
}

class Console extends React.Component<object, State> {
    constructor(props: object) {
        super(props);

        this.state = {
            user: {},
            menus: [],
            page: 'dashboard'
        };

        service.post('/user/sign').then(user => {
            if (!user || !user.id) {
                location.href = 'console-sign-in.html';

                return;
            }

            this.setState({
                user: user
            });

            service.menu().then(menus => {
                if (!menus || menus.length == 0)
                    return;

                this.setState({
                    menus: menus
                });
            });
        });
    }

    render(): JSX.Element {
        document.title = message.get('ranch.console.title');
        let height = document.body.clientHeight;

        return (
            <div id="ranch-ui-console">
                <div className="layout-top">
                    <div className="container">
                        <a href="javascript:void(0);" onClick={() => this.signOut()}>{message.get('ranch.console.sign-out')}</a>
                        <a href="javascript:void(0);">{this.state.user.id || ''}</a>
                    </div>
                </div>
                {this.left(height)}
                <div className="layout-bottom" style={{ top: height - 20 + 'px' }}>
                    Power by <a href="https://github.com/heisedebaise/ranch" target="_blank">ranch-ui</a>
                    &nbsp;&copy; {new Date().getFullYear()}
                </div>
                <Content />
            </div>
        );
    }

    private left(height: number): JSX.Element {
        return (
            <div className="layout-left" style={{ height: height - 52 + 'px' }}>
                {this.state.menus.map((menu, index) =>
                    <Accordion key={index} subject={menu.name}>
                        {menu.items.map((item, index) =>
                            <a key={index} href="javascript:void(0);" onClick={() => service.to(item.service)}>{item.name}</a>
                        )}
                    </Accordion>
                )}
            </div>
        );
    }

    private signOut(): void {
        service.post('/user/sign-out').then(data => {
            location.href = 'console-sign-in.html';
        });
    }
}

ReactDOM.render(
    <Console />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
