import React from 'react';
import { Upload, Icon, Modal } from 'antd';
import { url, service } from '../http';
import './image.css';

class Image extends React.Component {
    constructor(props) {
        super(props);

        this.uri = undefined;
        this.state = {
            loading: false,
            preview: null,
            remove: 0
        };
    }

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

                this.uri = this.uri ? (this.uri + ',' + data.path) : data.path;
                this.setState({ loading: false });
            });
        };
        reader.readAsDataURL(uploader.file);
    }

    preview = file => {
        this.setState({ preview: file.url });
    }

    cancel = () => {
        this.setState({ preview: null });
    }

    remove = file => {
        let uris = this.uri.split(',');
        this.uri = '';
        for (let i in uris) {
            if (i === file.uid) continue;

            if (this.uri.length > 0) this.uri += ',';
            this.uri += uris[i];
        }
        this.setState({ remove: this.state.remove + 1 });
    }

    render = () => {
        if (this.uri === undefined) this.uri = this.props.value;
        let list = [];
        if (this.uri) {
            let uris = this.uri.split(',');
            for (let i in uris) {
                list.push({
                    uid: '' + i,
                    name: uris[i],
                    url: url(uris[i]),
                    status: 'done'
                });
            }
        }

        return (
            <div className="clearfix">
                <input type="hidden" id={'image-' + this.props.upload.replace(/[^a-zA-Z0-9]+/g, '_')} value={this.uri || ''} />
                <Upload listType="picture-card" fileList={list} className="image-uploader" customRequest={this.upload} onPreview={this.preview} onRemove={this.remove} >
                    {this.props.size > 0 && list.length >= this.props.size ? null : <Icon type={this.state.loading ? 'loading' : 'plus'} />}
                </Upload>
                <Modal visible={this.state.preview != null} footer={null} onCancel={this.cancel}>
                    <img alt="preview" style={{ width: '100%' }} src={this.state.preview} />
                </Modal>
            </div>
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