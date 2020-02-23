import React from 'react';
import { Upload, Icon, Modal } from 'antd';
import { url, service } from '../http';
import './image.css';

class Image extends React.Component {
    state = {
        uri: null,
        changed: false,
        loading: false,
        preview: null,
        remove: 0
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

                let uri = this.state.changed ? this.state.uri : this.props.value;
                console.log(uri);
                uri = uri ? (uri + ',' + data.path) : data.path;
                console.log(uri);
                this.setState({
                    uri: uri,
                    changed: true,
                    loading: false
                }, () => {
                    console.log(this.state);
                    this.props.form.value(this.props.name, this.state.uri)
                });
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
        let uri = this.state.changed ? this.state.uri : this.props.value;
        if (!uri) return;

        let uris = uri.split(',');
        let u = '';
        for (let i in uris) {
            if (i === file.uid) continue;

            if (u.length > 0) u += ',';
            u += uris[i];
        }
        this.setState({
            uri: u,
            changed: true
        }, () => this.props.form.value(this.props.name, this.state.uri));
    }

    render = () => {
        let uri = this.state.changed ? this.state.uri : this.props.value;
        if (!this.state.changed && this.props.value) {
            this.props.form.value(this.props.name, this.props.value);
        }
        let list = [];
        if (uri) {
            let uris = uri.split(',');
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

export default Image;