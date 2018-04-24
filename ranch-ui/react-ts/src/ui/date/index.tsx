import * as React from 'react';
import { ComponentProps, Component } from '../basic/component';
import message from '../../util/message';
import './i18n'

declare const $: any;

interface Props extends ComponentProps {
    name?: string;
}

export default class Date extends Component<Props, object> {
    private dateId: string;

    constructor(props: Props) {
        super(props);

        this.dateId = this.getId('date');
    }

    componentDidMount(): void {
        $('#' + this.dateId).datepicker({
            dateFormat: 'yy-mm-dd',
            monthNames: message.get('date.month-names'),
            monthNamesShort: message.get('date.month-names'),
            dayNames: message.get('date.day-names'),
            dayNamesMin: message.get('date.day-names'),
            dayNamesShort: message.get('date.day-names')
        });
    }

    render(): JSX.Element {
        return <input id={this.dateId} {...this.props} />;
    }
}