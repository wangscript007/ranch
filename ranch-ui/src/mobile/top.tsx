import * as React from 'react';
import './top.less';

export class Top extends React.Component {
    render(): JSX.Element {
        return (
            <div className="layout-top">
                <div className="title">{this.props.children}</div>
            </div>
        );
    }
}