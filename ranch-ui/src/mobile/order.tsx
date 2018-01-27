import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../registerServiceWorker';
import storage from '../util/storage';
import Icon from '../ui/icon';
import { service } from './service';
import { Top } from './top';
import { Bottom } from './bottom';
import './order.less';

class Mine extends React.Component<object, object> {
    constructor(props: object) {
        super(props);

        service.signIn('order');
    }

    render(): JSX.Element {
        document.title = storage.title();

        return (
            <div id="ranch-ui-mobile">
                <Top>{storage.title()}</Top>
                <div className="layout-content">
                    <div className="order-empty">
                        <Icon code="&#xe604;" />
                    </div>
                </div>
                <Bottom active={1} />
            </div>
        );
    }
}

ReactDOM.render(
    <Mine />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
