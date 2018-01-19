import * as React from 'react';
import selector from '../../util/selector';
import http from '../../util/http';
import { ComponentProps, Component } from '../basic/component';
import Icon from '../icon';
import './index.less';

interface Props extends ComponentProps {
    name: string;
    fieldName: string;
}

interface State {
    path: string;
}

export default class Image extends Component<Props, State> {
    private imageId: string;

    constructor(props: Props) {
        super(props);

        let path = '';
        if (typeof props.defaultValue === 'string')
            path = props.defaultValue;
        this.state = {
            path: path
        };
        this.imageId = this.getId('image');
    }

    render(): JSX.Element {
        return (
            <div id={this.imageId} className={this.getClassName('ranch-ui-image')} ui-type="image" onClick={() => this.click()}>
                <input type="hidden" name={this.props.name} value={this.state.path} />
                <input type="file" onChange={() => this.change()} />
                {this.state.path ? <img src={this.state.path} /> : <Icon code="&#xe605;" />}
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
            let fileName: string = file.value.replace(/\\+/g, '/');
            http.post('/tephra/ctrl/upload', {}, {
                fieldName: this.props.fieldName,
                fileName: fileName.substring(fileName.lastIndexOf('/') + 1),
                contentType: reader.result.substring(reader.result.indexOf(':') + 1, reader.result.indexOf(';')),
                base64: reader.result.substring(reader.result.indexOf(',') + 1)
            }).then(json => {
                this.setState({
                    path: json.data.path
                });
            });
        }
        reader.readAsDataURL(file.files[0]);
    }

    private getFile(): any {
        return selector.find('#' + this.imageId + ' input[type="file"]');
    }
}