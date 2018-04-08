import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../registerServiceWorker';
import Slider from '../ui/slider';
import Page from './page';

class Mobile extends Page {
    protected getContent(): JSX.Element | JSX.Element[] | null {
        return <Slider slides={[
            <div>hello 111</div>,
            <div>hello 222</div>,
            <div>hello 333</div>
        ]} auto={2000} />;
    }
}

ReactDOM.render(
    <Mobile />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
