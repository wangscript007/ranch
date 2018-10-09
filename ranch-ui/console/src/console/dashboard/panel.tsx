import * as React from 'react';
import { Card } from 'antd';
import './panel.scss';

interface Props {
    title?: string;
    extra?: string | React.ReactNode;
    actions?: React.ReactNode[];
}

export default class Panel extends React.Component<Props> {
    public render(): JSX.Element {
        return (
            <Card title={this.props.title} type="inner" hoverable={true} extra={this.props.extra} actions={this.props.actions}>
                {this.props.children}
            </Card>
        );
    }
}