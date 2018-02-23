import * as React from 'react';
import merger from '../../../util/merger';
import { Prop, Page } from '../../meta';
import { PageComponent, PageProps, PageState, Toolbar } from '../index';
import './index.less';
import './index.css';

export default class Form extends PageComponent<PageProps, PageState> {
    constructor(props: PageProps) {
        super(props);
        this.state = {
            data: {}
        };
    }

    render(): JSX.Element {
        let page: Page = this.props.meta[this.props.service.substring(this.props.service.lastIndexOf('.') + 1)];
        let data = merger.merge({}, this.props.data, this.state.data);
        let hiddens: JSX.Element[] = [];
        hiddens.push(<input type="hidden" name="id" value={data['id'] || ''} key="id" />);
        let visibles: Prop[] = [];
        this.putElements(this.props.meta.props, hiddens, visibles, data);

        return (
            <form className="form" action="javascript:void(0);">
                {hiddens}
                <table cellSpacing="0">
                    <tbody>
                        {visibles.map((prop, index) => this.tr(index, prop, data))}
                    </tbody>
                </table>
                <Toolbar meta={this.props.meta} ops={page.toolbar} form=".form" />
            </form>
        );
    }

    protected putElements(props: Prop[], hiddens: JSX.Element[], visibles: Prop[], data: object): void {
        props.map((prop, index) => {
            if (prop.type === 'hidden')
                hiddens.push(<input type="hidden" name={prop.name} value={data[prop.name] || ''} key={index} />);
            else
                visibles.push(prop);
        });
    }

    protected tr(index: number, prop: Prop, data: object): JSX.Element | null {
        if (this.ignore(prop))
            return null;

        return (
            <tr className="line" key={index}>
                <td className="label">{prop.label}</td>
                <td className="data"><div className={prop.type || 'text'}>{this.input(prop, data)}</div></td>
                <td />
            </tr>
        )
    }
}