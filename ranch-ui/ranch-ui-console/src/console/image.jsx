import React from 'react';
import { Upload, Icon } from 'antd';
import { url, service } from '../http';
import './image.css';

class Image extends React.Component {
    state = {
        loading: false,
    };

    upload = (uploader) => {
        this.setState({ loading: true });

        let reader = new FileReader();
        reader.onload = () => {
            if (!reader.result || typeof reader.result !== 'string') {
                return;
            }

            service('/tephra/ctrl/upload', {
                name: this.props.upload,
                fileName: uploader.file.name,
                contentType: uploader.file.type,
                base64: reader.result.substring(reader.result.indexOf(',') + 1)
            }).then(data => {
                if (data === null) return;

                this.setState({
                    loading: false,
                    uri: data.path
                });
            });
        };
        reader.readAsDataURL(uploader.file);
    }

    render = () => {
        let uri = this.state.uri || this.props.value;

        return (
            <Upload listType="picture-card" className="image-uploader" showUploadList={false} customRequest={this.upload} >
                <input type="hidden" id={'image-' + this.props.upload.replace(/[^a-zA-Z0-9]+/g, '_')} value={uri || ''} />
                {uri ? <img src={url(uri)} alt="upload" style={{ width: '100%' }} /> : <Icon type={this.state.loading ? 'loading' : 'plus'} />}
            </Upload>
        );
    }
}

const getImageUri = upload => {
    let input = document.querySelector('#image-' + upload.replace(/[^a-zA-Z0-9]+/g, '_'));

    return input ? input.value : '';
}

export {
    Image,
    getImageUri
};