import * as React from 'react';
import { pager, MetaProp, PageMeta } from './pager';
import Grid from './grid';
import Form from './form';
import Dashboard from './dashboard';
import './page.scss';

export interface PageState {
    service: string;
    parameter?: {};
    header?: {};
    meta: PageMeta;
    props: MetaProp[];
    data?: any;
    form?:any;
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
        if (this.state.meta.type === 'grid') {
            return <Grid {...this.state} />
        }

        if (this.state.meta.type === 'form') {
            return <Form {...this.state} />
        }

        return <Dashboard {...this.state} />
    }
}