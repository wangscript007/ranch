import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../registerServiceWorker';
import message from '../util/message';
import Slider from '../ui/slider';
import { Top } from './top';
import { Bottom } from './bottom';
import './i18n';
import './index.less';

class Mobile extends React.Component {
    render(): JSX.Element {
        document.title = message.get('title');

        return (
            <div id="ranch-ui-mobile">
                <Top>{message.get('title')}</Top>
                <div className="layout-content">
                    <Slider images={['img/1.jpg', 'img/2.jpg', 'img/3.jpg']} links={['index.html', 'order.html', 'mine.html']} />
                </div>
                <Bottom active={0} />
            </div>
        );
    }
}

ReactDOM.render(
    <Mobile />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
