import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../../registerServiceWorker';
import Icon from '../../ui/icon';
import { service } from '../service';
import Page from '../page';
import './index.less';

export interface Doc {
    id?: string;
    image?: string;
    subject: string;
    content?: string;
}

interface State {
    list: Doc[];
}

class DocQuery extends Page<object, State> {
    constructor(props: object) {
        super(props);
        this.state = {
            list: []
        };
        service.post('/doc/query-by-key', {}, { key: 'setting.doc' }).then(data => {
            if (data === null)
                return;

            this.setState(data);
        });
    }

    protected getTitle(): string {
        return 'doc';
    }

    protected getContent(): JSX.Element | JSX.Element[] | null {
        return (
            <div className="docs">
                {this.state.list.map((doc, index) =>
                    <div className="doc" key={index} onClick={() => location.href = 'doc-read.html?doc,' + doc.id}>
                        {doc.image ? <img src={doc.image} /> : null}
                        <div className="forward-to">
                            <Icon code="&#xe60f;" />{doc.subject}
                        </div>
                    </div>
                )}
            </div>
        );
    }

    protected getBottom(): number {
        return 1;
    }
}

ReactDOM.render(
    <DocQuery />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
