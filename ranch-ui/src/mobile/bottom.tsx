import * as React from 'react';
import Icon from '../ui/icon';
import './bottom.less';

interface Props {
    active: number;
}

export class Bottom extends React.Component<Props, object> {
    render(): JSX.Element {
        return (
            <div className="layout-bottom">
                <table cellSpacing="0">
                    <tbody>
                        <tr>
                            <BottomItem href="index.html" icon="&#xe602;" active={this.props.active == 0}>首页</BottomItem>
                            <BottomItem href="order.html" icon="&#xe604;" active={this.props.active == 1}>订单</BottomItem>
                            <BottomItem href="mine.html" icon="&#xe603;" active={this.props.active == 2}>我的</BottomItem>
                        </tr>
                    </tbody>
                </table>
            </div>
        );
    }
}

interface ItemProps {
    href: string;
    icon: string;
    active?: boolean;
}

export class BottomItem extends React.Component<ItemProps, object> {
    render(): JSX.Element {
        if (this.props.active) {
            return (
                <td className="active">
                    <Icon code={this.props.icon} />
                    <div className="label">{this.props.children}</div>
                </td>
            );
        }

        return (
            <td>
                <a href={this.props.href} className="inactive">
                    <Icon code={this.props.icon} />
                    <div className="label">{this.props.children}</div>
                </a>
            </td>
        );
    }
}