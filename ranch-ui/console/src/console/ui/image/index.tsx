import * as React from 'react';
import { Upload, Icon } from 'antd';
import http from '../../../util/http';
import { service } from '../../../service';
import './index.scss';

interface Props {
    name: string;
    upload: string;
    value?: string;
}

interface State {
    loading: boolean;
    value?: string;
}

export class Image extends React.Component<Props, State> {
    public constructor(props: Props) {
        super(props);

        this.state = {
            loading: false,
            value: props.value
        };
        this.upload = this.upload.bind(this);
    }

    public render(): JSX.Element {
        const value = this.state.value || this.props.value;

        return (
            <Upload name={this.props.name} listType="picture-card" className="avatar-uploader" showUploadList={false} beforeUpload={this.upload}>
                <input type="hidden" id={'image-' + this.props.name.replace(/[^a-zA-Z0-9]+/g, '_')} value={value} />
                {value ? <img src={http.url(value)} /> : <Icon type={this.state.loading ? 'loading' : 'plus'} />}
            </Upload>
        );
    }

    private upload(file: File): boolean {
        const reader: FileReader = new FileReader();
        reader.onload = () => {
            if (!reader.result || typeof reader.result !== 'string') {
                return;
            }

            this.setState({ loading: true });
            service.post('/tephra/ctrl/upload', {}, {
                name: this.props.upload,
                fileName: file.name,
                contentType: file.type,
                base64: reader.result.substring(reader.result.indexOf(',') + 1)
            }).then(data => {
                if (data === null) {
                    return;
                }

                this.setState({
                    loading: false,
                    value: data.path
                });
            });
        };
        reader.readAsDataURL(file);

        return false;
    }
}

export function getImageValue(name: string): string {
    const input: HTMLInputElement | null = document.querySelector('#image-' + name.replace(/[^a-zA-Z0-9]+/g, '_'));

    return input ? input.value : '';
}