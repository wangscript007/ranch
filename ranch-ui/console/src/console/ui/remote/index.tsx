import * as React from 'react';
import { Select } from 'antd';
import { pager } from '../../pager';
import './index.scss';

interface Props {
    getFieldDecorator?: any;
    service: string;
    header?: object;
    parameter?: object;
    value?: string;
    label: string[];
}

interface State {
    list: Array<{}>;
}

export default class Remote extends React.Component<Props, State>{
    public constructor(props: Props) {
        super(props);

        this.state = {
            list: []
        };

        pager.post({
            service: this.props.service,
            header: this.props.header,
            parameter: this.props.parameter
        }).then(data => {
            if (data === null) {
                return;
            }

            this.setState({
                list: Array.isArray(data) ? data : data.list
            })
        });
    }

    public render(): JSX.Element {
        if (this.props.getFieldDecorator) {
            return this.props.getFieldDecorator(
                <Select style={{ minWidth: 200 }}>
                    {this.state.list.map((obj, index) =>
                        <Select.Option key={index} value={obj[this.props.value || 'id']}>{this.label(obj)}</Select.Option>
                    )}
                </Select>
            );
        }

        return (
            <Select style={{ minWidth: 200 }}>
                {this.state.list.map((obj, index) =>
                    <Select.Option key={index} value={obj[this.props.value || 'id']}>{this.label(obj)}</Select.Option>
                )}
            </Select>
        );
    }

    private label(obj: object): string {
        let label = '';
        for (const str of this.props.label) {
            if (!obj.hasOwnProperty(str)) {
                continue;
            }

            label += obj[str] + ' ';
        }

        return label.trim();
    }
}