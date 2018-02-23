import * as React from 'react';
import { ComponentProps, Component } from '../basic/component';
import './index.less';

interface Props extends ComponentProps {
    subject: string;
    show?: boolean;
}

interface State {
    subject: string;
    show: boolean;
}

export default class Accordion extends Component<Props, State> {
    constructor(props: Props) {
        super(props);
        this.state = {
            subject: props.subject,
            show: props.show || false
        };
    }

    render(): JSX.Element {
        return (
            <div id={this.getId('accordion')} className={this.getClassName('ranch-ui-accordion')} ui-type="accordion">
                <div className="accordion-subject" onClick={() => this.click()}>{this.state.subject}</div>
                <div className="accordion-content" style={{ display: this.state.show ? '' : 'none' }}>
                    {this.props.children}
                </div>
            </div>
        );
    }

    private click(): void {
        this.setState(prevState => ({
            show: !prevState.show
        }));
    }
}