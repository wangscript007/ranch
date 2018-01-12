import * as React from 'react';
import './top.less';

interface Props {
}

export class Top extends React.Component<Props, object> {
    render(): JSX.Element {
        return (
            <div className="layout-top">
                <div className="title">{this.props.children}</div>
            </div>
        );
    }
}