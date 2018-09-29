import * as React from 'react';
import { PropMeta, PageMeta } from './meta';
import { pager } from './pager';
import Grid from './grid';
import Form from './form';
import Dashboard from './dashboard';
import Settings from './settings';
import './page.scss';

export interface PageState {
    service: string;
    parameter?: {};
    header?: {};
    meta: PageMeta;
    props: PropMeta[];
    data?: any;
}

export class Page extends React.Component<object, PageState> {
    public constructor(props: object) {
        super(props);
        this.state = {
            service: '',
            meta: {
                type: ''
            },
            props: []
        };
        pager.bind(this);
    }

    public render(): JSX.Element {
        if (this.state.service === 'blank') {
            return <div className="page" />;
        }

        return <div className="page">{this.page()}</div>
    }

    private page(): JSX.Element {
        switch (this.state.meta.type) {
            case 'grid':
                return <Grid {...this.state} />;
            case 'form':
                return <Form {...this.state} />;
            case 'settings':
                return <Settings {...this.state} />;
            default:
                return <Dashboard {...this.state} />;
        }
    }
}