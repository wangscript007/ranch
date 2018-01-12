import * as React from 'react';
import generator from '../util/generator';
import { Meta } from './meta';
import { service } from './service';
import Dashboard from './page/dashboard';
import Grid from './page/grid';
import Form from './page/form';

const pages = {
    dashboard: Dashboard,
    grid: Grid,
    form: Form
};

interface Props {

}

interface State {
    page: string;
    service?: string;
    meta?: Meta;
}

export class Content extends React.Component<Props, State> {
    constructor(props: Props) {
        super(props);
        this.state = {
            page: "dashboard"
        };
        service.bind(this);
    }

    render(): JSX.Element {
        let page = {
            tag: pages[this.state.page] || Dashboard,
            props: this.state
        };

        return (
            <div className="layout-content">
                <page.tag {...page.props} random={generator.random(8)} />
            </div>
        );
    }
}