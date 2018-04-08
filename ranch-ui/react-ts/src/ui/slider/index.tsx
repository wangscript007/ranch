import * as React from 'react';
import { ComponentProps, Component } from '../basic/component';
import './index.less';

declare const Swiper: any;

interface Props extends ComponentProps {
    slides: JSX.Element[];
    auto?: number;
    number?: boolean;
}

export default class Slider extends Component<Props, object> {
    private sliderId: string;
    constructor(props: Props) {
        super(props);

        this.sliderId = this.getId('slider');
    }

    componentDidMount(): void {
        let options: any = {
            autoHeight: true,
            pagination: {
                el: '#' + this.sliderId + ' .swiper-pagination',
                clickable: true,
                renderBullet: (index: number, className: string) => {
                    return '<span class="' + className + '">' + (this.props.number ? (index + 1) : '') + '</span>';
                }
            }
        };
        if (this.props.auto) {
            options.autoplay = {
                delay: this.props.auto,
                disableOnInteraction: false,
            }
        }
        new Swiper('#' + this.sliderId, options);
    }

    render(): JSX.Element {
        return (
            <div id={this.sliderId} className={this.getClassName('ranch-ui-slider')} ui-type="slider">
                <div className="swiper-wrapper">{this.props.slides.map((slide, index) => <div className="swiper-slide">{slide}</div>)}</div>
                <div className="swiper-pagination" />
            </div>
        );
    }
}