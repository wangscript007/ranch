import * as React from 'react';
import selector from '../../util/selector';
import { ComponentProps, Component } from '../basic/component';
import Icon from '../icon';
import './index.less';

interface Props extends ComponentProps {
    name: string;
}

interface State {
    data: string;
}

export default class Base64 extends Component<Props, State> {
    private fileId: string;

    constructor(props: Props) {
        super(props);

        let data = '';
        if (typeof props.defaultValue === 'string')
            data = props.defaultValue;
        this.state = {
            data: data
        };
        this.fileId = this.getId('base64');
    }

    render(): JSX.Element {
        let props = {
            id: this.fileId,
            className: this.getClassName('ranch-ui-base64'),
            'ui-type': 'base64'
        };

        return (
            <div {...props} onClick={() => this.click()}>
                <input type="hidden" name={this.props.name} value={this.state.data} />
                <input type="file" onChange={() => this.change()} />
                <Icon code="&#xe605;" />
                {this.state.data ? this.state.data : ''}
            </div>
        );
    }

    private click(): void {
        this.getFile().click();
    }

    private change(): void {
        let file = this.getFile();
        let reader: FileReader = new FileReader();
        reader.onload = () => {
            this.setState({
                data: reader.result.substring(reader.result.indexOf(',') + 1)
            });
        }
        reader.readAsDataURL(file.files[0]);
    }

    private getFile(): any {
        return selector.find('#' + this.fileId + ' input[type="file"]');
    }
}