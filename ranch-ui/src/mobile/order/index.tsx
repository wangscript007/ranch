import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../../registerServiceWorker';
import Icon from '../../ui/icon';
import { service } from '../service';
import Page from '../page';
import './i18n';
import './index.less';

class Order extends Page {
    constructor(props: object) {
        super(props);

        service.signIn('order');
    }

    protected getTitle(): string {
        return 'order';
    }

    protected getContent(): JSX.Element | JSX.Element[] | null {
        return (
            <div className="order-empty">
                <Icon code="\ue604" />
            </div>
        );
    }

    protected getBottom(): number {
        return 1;
    }
}

ReactDOM.render(
    <Order />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
