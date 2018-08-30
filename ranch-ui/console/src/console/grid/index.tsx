import * as React from 'react';
import { Button, Table, Divider } from 'antd';
import merger from '../../util/merger';
import { pager, Action, Model } from '../pager';
import { PageState } from '../page';
import './index.scss';

interface State {
    list?: Array<{}>;
    size?: number;
    number?: number;
}

export default class Grid extends React.Component<PageState, State> {
    public constructor(props: PageState) {
        super(props);
        this.state = {};
    }

    public render(): JSX.Element[] {
        const elements: JSX.Element[] = [];
        this.toolbar(elements);
        this.table(elements);

        return elements;
    }

    private toolbar(elements: JSX.Element[]): void {
        if (!this.props.meta.toolbar) {
            return;
        }

        elements.push(<div key="grid-toolbar" className="grid-toolbar">{this.props.meta.toolbar.map((button) =>
            <Button key={button.type} type="primary" icon={button.icon} onClick={this.click.bind(this, button)}>{button.label}</Button>
        )}</div>);
    }

    private table(elements: JSX.Element[]): void {
        const columns: Array<{}> = [];
        for (const prop of this.props.props) {
            if (prop.labels && prop.labels.length > 0) {
                columns.push({
                    title: prop.label,
                    render: (model: Model) => {
                        return (prop.labels || [])[model[prop.name]];
                    }
                });

                continue;
            }

            columns.push({
                title: prop.label,
                dataIndex: prop.name,
                key: prop.name
            });
        }
        if (this.props.meta.ops && this.props.meta.ops.length > 0) {
            columns.push({
                title: '',
                render: (model: Model) => {
                    const ops: JSX.Element[] = [];
                    for (const op of this.props.meta.ops || []) {
                        if (op.when && !eval(op.when)) {
                            continue;
                        }

                        if (ops.length > 0) {
                            ops.push(<Divider key={'divider-' + ops.length} type="vertical" />);
                        }
                        ops.push(<a key={'op-' + ops.length} href="javascript:void(0);" onClick={this.click.bind(this, op, model)}>{op.label}</a>);
                    }

                    return ops;
                }
            });
        }

        let dataSource: Array<{}> = [];
        let pagination: object | boolean = false;
        if (this.props.data) {
            const hasList = this.props.data.hasOwnProperty('list');
            dataSource = hasList ? this.props.data.list : this.props.data;
            if (hasList) {
                pagination = {
                    pageSize: this.state.size,
                    current: this.state.number
                };
            }
        }

        elements.push(<Table key="grid-table" rowKey="id" columns={columns} dataSource={dataSource} pagination={pagination} />);
    }

    private click(action: Action, model?: Model): void {
        const key: string = pager.getService(this.props.service, action);
        console.log(key);
        console.log(action);
        pager.getMeta(key).then(meta => {
            if (meta === null) {
                return;
            }

            if (action.type === 'create') {
                pager.setPage(key, meta, meta[action.type], {}, this.props.header, this.props.parameter);

                return;
            }

            if (action.type === 'modify') {
                pager.setPage(key, meta, meta[action.type], model, this.props.header, this.props.parameter);

                return;
            }

            if (action.type === 'delete' && model) {
                pager.post(key, this.props.header, merger.merge(this.props.parameter || {}, model)).then(data => {
                    if (data === null || !action.success) {
                        return;
                    }

                    pager.success(this.props.service, action, this.props.header, this.props.parameter, model);
                });

                return;
            }

            console.log(action);
        });
    }
}