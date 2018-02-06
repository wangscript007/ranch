import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../../registerServiceWorker';
import { service } from '../service';
import Panel from '../panel';
import { Doc } from './index';
import './read.less';

class DocRead extends Panel<object, Doc> {
    constructor(props: object) {
        super(props);

        this.state = {
            subject: '',
            content: ''
        };
        service.post('/doc/read-json', {}, { id: service.getArgsFromLocation()[1] }).then(data => {
            if (data === null)
                return;

            this.setState(data);
        });
    }

    protected getTitle(): string {
        return this.state.subject;
    }

    protected getContent(): JSX.Element | JSX.Element[] | null {
        return [
            <div className="image">{this.state.image ? <img src={this.state.image} /> : null}</div>,
            <div className="content" dangerouslySetInnerHTML={{ __html: this.state.content || '' }} />
        ];
    }
}

ReactDOM.render(
    <DocRead />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
