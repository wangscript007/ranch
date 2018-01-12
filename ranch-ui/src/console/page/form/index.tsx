import * as React from 'react';
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

        return (
            <form className="form" action="javascript:void(0);">
                <input type="hidden" name="id" value={this.props.data['id'] || ''} />
                <table cellSpacing="0">
                    <tbody>
                        {this.props.meta.props.map((prop, index) => (
                            <tr className="line" key={index}>
                                <td className="label">{prop.label}</td>
                                <td className="data"><div className={prop.type || 'text'}>{this.input(prop)}</div></td>
                                <td></td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                <Toolbar meta={this.props.meta} ops={page.toolbar} />
            </form>
        );
    }

    input(prop: Prop): JSX.Element {
        let value = this.props.data[prop.name] || "";
        if (prop.type === 'read-only')
            return value;

        let props = {
            name: prop.name,
            defaultValue: value
        };
        if (prop.type === 'text-area')
            return <textarea {...props} />

        if (prop.labels && prop.labels.length > 0) {
            return (
                <select {...props} >
                    {prop.labels.map((label, index) => <option key={index} value={index}>{label}</option>)}
                </select>
            );
        }

        return <input type={prop.type || 'text'} {...props} />;
    }
}