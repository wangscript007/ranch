import * as React from 'react';
import { ComponentProps, Component } from '../basic/component';
import './index.less';

interface Props extends ComponentProps {
    images: string[];
    links: string[];
}

interface State {
    index: number;
}

export default class Slider extends Component<Props, State> {
    constructor(props: Props) {
        super(props);
        this.state = {
            index: 0
        };
    }

    render(): JSX.Element {
        let index = this.state.index || 0;
        setTimeout(() => {
            this.setState({
                index: (index + 1) % this.props.images.length
            });
        }, 5000);

        return (
            <div id={this.getId('slider')} className={this.getClassName('slider')} ui-type="slider">
                <a href={this.props.links[index]}><img src={this.props.images[index]} /></a>
            </div>
        );
    }
}