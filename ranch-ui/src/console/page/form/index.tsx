import * as React from 'react';
import Image from '../../../ui/image';
import { Prop, Page } from '../../meta';
import { PageComponent, PageProps, PageState, Toolbar } from '../index';
import './index.less';

export default class Form extends PageComponent<PageProps, PageState> {
    constructor(props: PageProps) {
        super(props);
        this.state = {
            data: {}
        };
    }

    render(): JSX.Element {
        this.refresh();
        let page: Page = this.props.meta[this.props.service.substring(this.props.service.lastIndexOf('.') + 1)];
        let data = this.props.data || {};
        let hiddens: JSX.Element[] = [];
        hiddens.push(<input type="hidden" name="id" value={data['id'] || ''} key="id" />);
        let props: Prop[] = [];
        this.props.meta.props.map((prop, index) => {
            if (prop.type === 'hidden')
                hiddens.push(<input type="hidden" name={prop.name} value={data[prop.name] || ''} key={index} />);
            else
                props.push(prop);
        });

        return (
            <form className="form" action="javascript:void(0);">
                {hiddens}
                <table cellSpacing="0">
                    <tbody>
                        {props.map((prop, index) => this.tr(index, prop, data))}
                    </tbody>
                </table>
                <Toolbar meta={this.props.meta} ops={page.toolbar} />
            </form>
        );
    }

    private tr(index: number, prop: Prop, data: object): JSX.Element {
        return (
            <tr className="line" key={index}>
                <td className="label">{prop.label}</td>
                <td className="data"><div className={prop.type || 'text'}>{this.input(prop, data)}</div></td>
                <td></td>
            </tr>
        )
    }

    private input(prop: Prop, data: object): JSX.Element {
        let value = data[prop.name] || "";
        if (prop.type === 'read-only')
            return value;

        let props = {
            name: prop.name,
            defaultValue: value
        };
        if (prop.type === 'text-area')
            return <textarea {...props} />

        if (prop.type === 'image')
            return <Image {...props} fieldName={prop['fieldName'] || this.props.meta.key + '.' + prop.name} />

        if (prop.labels && prop.labels.length > 0) {
            return (
                <select {...props} >
                    {prop.labels.map((label, index) => <option key={index} value={index}>{label}</option>)}
                </select>
            );
        }

        if (prop.values) {
            let options: JSX.Element[] = [];
            for (let value in prop.values)
                options.push(<option value={value}>{prop.values[value]}</option>);

            return <select {...props}>{options}</select>;
        }

        return <input type={prop.type || 'text'} {...props} />;
    }
}