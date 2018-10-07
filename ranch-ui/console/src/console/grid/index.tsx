import * as React from 'react';
import { Button, Table, Divider, Form, Menu, Dropdown, Icon } from 'antd';
import http from '../../util/http';
import merger from '../../util/merger';
import { meta, PropMeta, ActionMeta } from '../meta';
import { pager, Model } from '../pager';
import { PageState } from '../page';
import './index.scss';

interface Props extends PageState {
    form: any;
}

interface State {
    data: {
        list?: Array<{}>;
        size?: number;
        count?: number;
        number?: number;
    };
    pagination: boolean;
}

class Grid extends React.Component<Props, State> {
    public constructor(props: Props) {
        super(props);
        this.state = this.getState(this.props.data);
    }

    public render(): JSX.Element[] {
        const searchToolbar: JSX.Element[] = [];
        this.search(searchToolbar);
        this.toolbar(searchToolbar);
        const elements: JSX.Element[] = [];
        if (searchToolbar.length > 0) {
            elements.push(<Form key='search-toolbar' className="grid-search-toolbar" layout="inline">{searchToolbar}</Form>);
        }
        this.table(elements);

        return elements;
    }

    private search(searchToolbar: JSX.Element[]): void {
        if (!this.props.meta.search || this.props.meta.search.length === 0) {
            return;
        }

        const props: PropMeta[] = [];
        for (const s of this.props.meta.search) {
            let has: boolean = false;
            for (const p of this.props.props) {
                if (s.name === p.name) {
                    props.push(merger.merge({ name: '' }, p, s));
                    has = true;

                    break;
                }
            }
            if (!has) {
                props.push(s);
            }
        }

        props.map(prop =>
            searchToolbar.push(
                <Form.Item key={prop.name} label={prop.label} >
                    {pager.getInput(this.props.form, prop, {}, true)}
                </Form.Item>
            )
        );
        searchToolbar.push(<Button key="search" type="primary" icon="search" onClick={this.click.bind(this, { type: 'search' })}>搜索</Button>);
    }

    private toolbar(searchToolbar: JSX.Element[]): void {
        if (!this.props.meta.toolbar || this.props.meta.toolbar.length === 0) {
            return;
        }

        this.props.meta.toolbar.map(action =>
            searchToolbar.push(<Button key={action.type} type="primary" icon={action.icon} onClick={this.click.bind(this, action)}>{action.label}</Button>)
        );
    }

    private table(elements: JSX.Element[]): void {
        const columns: Array<{}> = [];
        columns.push({
            title: '',
            key: 'index',
            render: (text: Model, record: Model, index: number) => index + 1
        });

        for (const prop of this.props.props) {
            const column: any = {
                title: prop.label,
                key: prop.name
            };
            columns.push(column);

            if (prop.labels && prop.labels.length > 0) {
                column.render = (model: Model) => {
                    return (prop.labels || [])[pager.getModelValue(model, prop.name)];
                };

                continue;
            }

            if (prop.values) {
                column.render = (model: Model) => {
                    return (prop.values || {})[pager.getModelValue(model, prop.name)];
                };

                continue;
            }

            if (prop.type === 'image') {
                column.render = (model: Model) => {
                    const uri = pager.getModelValue(model, prop.name);

                    return uri ? <a href={http.url(uri)} target="_blank"><img key={prop.name} className="grid-img" src={http.url(uri)} /></a> : null;
                };

                continue;
            }

            if (prop.type === 'money') {
                column.render = (model: Model) => {
                    const value = pager.getModelValue(model, prop.name);
                    if (value === 0) {
                        return '0.00';
                    }

                    return <span className={value > 0 ? 'grid-money-positive' : 'grid-money-negative'}>{(value * 0.01).toFixed(2)}</span>;
                };

                continue;
            }

            column.dataIndex = prop.name;
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
                        ops.push(<a key={'op-' + ops.length} className="grid-op" href="javascript:void(0);" onClick={this.click.bind(this, op, model)}>{op.label}</a>);
                    }

                    if (ops.length <= 3) {
                        return ops;
                    }

                    const items: JSX.Element[] = [];
                    for (let i = 2; i < ops.length; i++) {
                        const op: JSX.Element = ops[i];
                        if (!op.key || typeof op.key !== 'string' || op.key.indexOf('divider-') === 0) {
                            continue;
                        }

                        items.push(<Menu.Item key={op.key}>{op}</Menu.Item>);
                    }
                    const actions: JSX.Element[] = [];
                    actions.push(ops[0]);
                    actions.push(ops[1]);
                    actions.push(
                        <Dropdown overlay={<Menu>{items}</Menu>} trigger={['click']}>
                            <a className="ant-dropdown-link grid-op" href="javascript:void(0);">更多<Icon type="down" /></a>
                        </Dropdown>
                    );

                    return actions;
                }
            });
        }

        let dataSource: Array<{}> = [];
        let pagination: object | boolean = false;
        if (this.state.data) {
            dataSource = this.state.data.list || [];
            if (this.state.pagination) {
                pagination = {
                    pageSize: this.state.data.size,
                    total: this.state.data.count,
                    current: this.state.data.number
                };
            }
        }

        elements.push(<Table key="grid-table" rowKey="id" columns={columns} dataSource={dataSource} pagination={pagination} onChange={this.click.bind(this, { type: 'search' }, null)} />);
    }

    private click(action: ActionMeta, model?: Model, pagination?: { current: number }): void {
        const service: string = pager.getService(this.props.service, action);
        meta.get(service).then(mt => {
            if (mt === null) {
                return;
            }

            if (action.type === 'search') {
                const page = { pageNum: pagination ? pagination.current : this.state.data.number };
                pager.post({
                    service: this.props.service,
                    header: this.props.header,
                    parameter: merger.merge(pager.getFormValue(this.props.form, this.props.meta.search || []), page, this.props.parameter || {})
                }).then(data => {
                    if (data === null) {
                        return;
                    }

                    this.setState(this.getState(data));
                });

                return;
            }

            if (action.type === 'create') {
                pager.setPage({ service: service, data: {}, header: this.props.header, parameter: this.props.parameter });

                return;
            }

            if (action.type === 'modify') {
                pager.setPage({ service: service, data: model, header: this.props.header, parameter: this.props.parameter });

                return;
            }

            if (action.type === 'delete' && model) {
                pager.post({
                    service: service,
                    header: this.props.header,
                    parameter: merger.merge(model, this.props.parameter || {})
                }).then(data => {
                    if (data === null || !action.success) {
                        return;
                    }

                    this.success(action, model);
                });

                return;
            }

            if (action.type === 'post' && model) {
                pager.post({
                    service: service,
                    header: this.props.header,
                    parameter: this.mergeParameter(action, model)
                }).then(data => {
                    if (data === null || !action.success) {
                        return;
                    }

                    this.success(action, model);
                });

                return;
            }

            if (action.type === 'to') {
                pager.to({ service: service, header: this.props.header, parameter: this.mergeParameter(action, model) });

                return;
            }

            console.log(action);
        });
    }

    private mergeParameter(action: ActionMeta, model?: Model): object {
        let parameter: object = model ? merger.merge({}, model) : {};
        if (action.parameter) {
            if (model) {
                for (const key in action.parameter) {
                    if (action.parameter.hasOwnProperty(key)) {
                        parameter[key] = model[action.parameter[key]] || action.parameter[key];
                    }
                }
            } else {
                parameter = merger.merge(parameter, action.parameter);
            }
        }

        return merger.merge(parameter, this.props.parameter || {});
    }

    private getState(data: any): State {
        if (Array.isArray(data)) {
            return {
                data: {
                    list: data
                },
                pagination: false
            };
        }

        return {
            data: data,
            pagination: true
        };
    }

    private success(action: ActionMeta, model?: Model): void {
        if (!action.success) {
            return;
        }

        const service = typeof action.success === 'string' ? action.success : action.success.service;
        if (service === 'search') {
            this.click({ type: 'search' });

            return;
        }

        pager.success(this.props.service, action, this.props.header, this.props.parameter, model);
    }
}

export default Form.create()(Grid);