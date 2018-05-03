import * as React from 'react';
import { ComponentProps, Component } from '../basic/component';
import Icon from '../icon';
import './index.less';

interface Props extends ComponentProps {
    name: string;
    list: any[];
    valueName?: string;
    labelName?: string;
}

interface State {
    value: string;
    checkeds: string[];
}

export default class Check extends Component<Props, State> {
    constructor(props: Props) {
        super(props);

        this.state = {
            value: this.getDefaultValue(),
            checkeds: this.getDefaultValue().split(',')
        };
    }

    render(): JSX.Element[] {
        let elements: JSX.Element[] = [];
        elements.push(<input type="hidden" name={this.props.name} value={this.state.value} />);
        if (!this.props.list)
            return elements;

        this.props.list.map((kv, index) => {
            let value: string = typeof kv === 'string' ? kv : kv[this.props.valueName || 'value'];
            let checked: boolean = false;
            for (let i = 0; i < this.state.checkeds.length; i++) {
                if (value === this.state.checkeds[i]) {
                    checked = true;

                    break;
                }
            }
            let label: string = typeof kv === 'string' ? kv : kv[this.props.labelName || 'label'];
            elements.push(
                <div className={this.getClassName('check')} onClick={() => { this.click(value, checked) }}>
                    <Icon code="&#xe633;" className="uncheck" style={{ display: checked ? 'none' : '' }} />
                    <Icon code="&#xe634;" className="checked" style={{ display: checked ? '' : 'none' }} />
                    {label}
                </div>
            );
        });

        return elements;
    }

    private click(value: string, checked: boolean): void {
        let checkeds: string[] = [];
        for (let i = 0; i < this.state.checkeds.length; i++)
            if (this.state.checkeds[i] !== value)
                checkeds.push(this.state.checkeds[i]);
        if (!checked)
            checkeds.push(value);
        this.setState({
            value: checkeds.length === 0 ? '' : checkeds.join(',').substring(1),
            checkeds: checkeds
        });
    }
}