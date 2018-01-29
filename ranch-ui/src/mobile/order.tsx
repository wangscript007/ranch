import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../registerServiceWorker';
import message from '../util/message';
import Icon from '../ui/icon';
import { service } from './service';
import { Top } from './top';
import { Bottom } from './bottom';
import './i18n';
import './order.less';

class Mine extends React.Component<object, object> {
    constructor(props: object) {
        super(props);

        service.signIn('order');
    }

    render(): JSX.Element {
        let title = message.get('title.order') + ' - ' + message.get('title');
        document.title = title;

        return (
            <div id="ranch-ui-mobile">
                <Top>{title}</Top>
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
