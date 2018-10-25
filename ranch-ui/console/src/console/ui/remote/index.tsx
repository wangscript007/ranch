import * as React from 'react';
import { Select, TreeSelect } from 'antd';
import { pager } from '../../pager';
import './index.scss';

interface Props {
    getFieldDecorator?: any;
    service: string;
    header?: object;
    parameter?: object;
    value?: string;
    label: string[];
    search?: boolean;
    tree?: boolean;
}

interface State {
    list: Array<any>;
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
        if (this.props.tree) {
            return this.props.getFieldDecorator ? this.props.getFieldDecorator(this.tree()) : this.tree();
        }

        return this.props.getFieldDecorator ? this.props.getFieldDecorator(this.select()) : this.select();
    }

    private tree(): JSX.Element {
        return (
            <TreeSelect style={{ minWidth: 200 }}>
                {this.treeNodes(this.state.list)}
            </TreeSelect>
        );
    }

    private treeNodes(list: Array<any>): JSX.Element[] {
        if (!list || list.length === 0) {
            return [];
        }

        return list.map((obj, index) =>
            <TreeSelect.TreeNode key={obj[this.props.value || 'id']} value={obj[this.props.value || 'id']} title={this.label(obj)}>
                {this.treeNodes(obj.children)}
            </TreeSelect.TreeNode>
        );
    }

    private select(): JSX.Element {
        return (
            <Select style={{ minWidth: 200 }}>
                {this.props.search ? <Select.Option value="">全部</Select.Option> : null}
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