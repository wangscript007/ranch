import * as React from 'react';
import { ComponentProps, Component } from '../basic/component';

interface Props extends ComponentProps {
    code: string;
}

export default class Icon extends Component<Props, object> {
    render(): JSX.Element {
        return <i id={this.getId('icon')} className={this.getClassName('iconfont')}
            title={this.props.title || ''} ui-type="iconfont">{this.props.code}</i>;
    }
}