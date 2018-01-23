import * as React from 'react';
import message from '../../../util/message';
import Icon from '../../../ui/icon';
import { Prop, Page, Operate } from '../../meta';
import { service } from '../../service';
import { PageComponent, PageProps, PageState, Toolbar, getSuccess } from '../index';
import './i18n';
import './index.less';

export default class Grid extends PageComponent<PageProps, PageState> {
    constructor(props: PageProps) {
        super(props);
        this.state = {
            data: []
        };
    }

    render(): JSX.Element {
        this.refresh();
        let pagination = this.state.data.hasOwnProperty('list');
        let list: object[] = pagination ? this.state.data['list'] : this.state.data;
        let page: Page = this.props.meta[this.props.service.substring(this.props.service.lastIndexOf('.') + 1)];
        let props: Prop[] = [];
        this.props.meta.props.map(prop => {
            if (prop.type !== 'hidden')
                props.push(prop);
        });

        return (
            <div className="grid">
                <table cellSpacing="1">
                    <thead>
                        <tr>
                            <th></th>
                            {props.map((prop, index) => <th key={index}>{prop.label}</th>)}
                            {page.ops ? <th></th> : ''}
                        </tr>
                    </thead>
                    <tbody>
                        {list.map((row, index) =>
                            <tr key={index}>
                                <th>{index + 1}</th>
                                {props.map((prop, index) => <td key={index} className={'data ' + (prop.type || '')}>{this.td(row, prop)}</td>)}
                                {this.ops(page.ops, row)}
                            </tr>
                        )}
                        {this.empty(list, props, page.ops)}
                    </tbody>
                </table>
                {this.pagination(pagination)}
                <Toolbar meta={this.props.meta} ops={page.toolbar} />
            </div>
        );
    }

    private td(row: object, prop: Prop): any {
        if (!row.hasOwnProperty(prop.name))
            return '';

        let value = row[prop.name];
        if (prop.type === 'image')
            return <img src={value} />;

        if (prop.labels)
            return prop.labels[value] || value;

        if (prop.values)
            return prop.values[value] || value;

        return value;
    }

    private ops(ops: Operate[], data: object): JSX.Element | string {
        if (!ops || ops.length == 0)
            return '';

        return (
            <td className="ops">
                {ops.map((op, index) => <a key={index} href="javascript:void(0);" onClick={() => this.op(op, data)}>{op.label}</a>)}
            </td>
        );
    }

    private op(op: Operate, data: object): void {
        if (op.type === 'modify') {
            service.to(this.props.meta.key + '.modify', {}, data);

            return;
        }

        if (op.type === 'delete') {
            service.execute(this.props.meta.key + '.delete', {}, { id: data['id'] }, getSuccess(this.props.meta, op, ".query"));

            return;
        }

        let key: string = op.service || '';
        if (key.charAt(0) == '.')
            key = this.props.meta.key + key;
        service.to(key, this.parameter(data, op.parameter));
    }

    private parameter(data: object, param?: object): object {
        if (!param)
            return data;

        let parameter = {};
        for (const key in param)
            parameter[key] = data[param[key]] || '';
        service.setParameter(parameter);

        return parameter;
    }

    private empty(list: object[], props: Prop[], ops: Operate[]): JSX.Element | null {
        if (list && list.length > 0)
            return null;

        return (
            <tr>
                <th>0</th>
                <td colSpan={props.length + (ops && ops.length > 0 ? 1 : 0)} className="empty">{message.get('grid.empty')}</td>
            </tr>
        );
    }

    private pagination(enable: boolean): JSX.Element | null {
        if (!enable || this.state.data.pageEnd <= 1)
            return null;

        let prev: JSX.Element[] = [];
        for (let i = this.state.data.pageStart; i < this.state.data.number; i++)
            prev.push(this.page(i));
        let next: JSX.Element[] = [];
        for (let i = this.state.data.number + 1; i <= this.state.data.pageEnd; i++)
            next.push(this.page(i));

        return (
            <div className="pagination">
                <a href="javascript:void(0);" onClick={() => this.pageTo(this.state.data.number - 1)}><Icon code="&#xe608;" /></a>
                {prev}
                <a href="javascript:void(0);" onClick={() => this.pageTo(this.state.data.number)} className="active">{this.state.data.number}</a>
                {next}
                <a href="javascript:void(0);" onClick={() => this.pageTo(this.state.data.number + 1)}><Icon code="&#xe609;" /></a>
            </div>
        );
    }

    private page(num: number): JSX.Element {
        return <a href="javascript:void(0);" onClick={() => this.pageTo(num)} key={num}>{num}</a>;
    }

    private pageTo(num: number): void {
        let parameter: object = this.props.parameter || {};
        parameter['pageNum'] = num;
        service.execute(this.props.service, {}, parameter).then(data => this.setState({ data: data }));
    }
}