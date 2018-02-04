import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../../registerServiceWorker';
import { service } from '../service';
import Panel from '../panel';
import './index.less';

interface State {
    name: string;
    value: string;
}

class Classify extends Panel<object, State> {
    constructor(props: object) {
        super(props);

        this.state = {
            name: '',
            value: ''
        };
        let args = service.getArgsFromLocation();
        if (args.length <= 2)
            return;

        service.classify(args[1], args[2]).then(classify => {
            if (classify === null)
                return;

            this.setState({
                name: classify.name || '',
                value: classify.value || ''
            });
        });
    }

    protected getTitle(): string {
        return this.state.name;
    }

    protected getContent(): JSX.Element | JSX.Element[] | null {
        return <div className="content" dangerouslySetInnerHTML={{ __html: this.state.value }} />;
    }
}

ReactDOM.render(
    <Classify />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
