import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../registerServiceWorker';
import Slider from '../ui/slider';
import Page from './page';

class Mobile extends Page {
    protected getContent(): JSX.Element | JSX.Element[] | null {
        return <Slider images={['img/1.jpg', 'img/2.jpg', 'img/3.jpg']} links={['index.html', 'order.html', 'mine.html']} />;
    }
}

ReactDOM.render(
    <Mobile />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
