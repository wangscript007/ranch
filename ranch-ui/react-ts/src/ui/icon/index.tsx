import * as React from 'react';
import { ComponentProps, Component } from '../basic/component';

interface Props extends ComponentProps {
    code: string;
}

export default class Icon extends Component<Props, object> {
    render(): JSX.Element {
        return <i id={this.getId('icon')} className={this.getClassName('ranch-ui-icon')}
            title={this.props.title || ''} ui-type="icon" {...this.props}>{this.props.code}</i>;
    }
}