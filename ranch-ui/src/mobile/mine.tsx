import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../registerServiceWorker';
import storage from '../util/storage';
import { Top } from './top';
import { Bottom } from './bottom';
import './mine.less';

class Mine extends React.Component<object, object> {
    render(): JSX.Element {
        document.title = storage.title();

        return (
            <div id="ranch-ui-mobile">
                <Top>{storage.title()}</Top>
                <div className="layout-content">
                    content
                </div>
                <Bottom active={2} />
            </div>
        );
    }
}

ReactDOM.render(
    <Mine />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
