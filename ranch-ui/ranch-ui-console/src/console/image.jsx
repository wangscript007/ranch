import React from 'react';
import { Upload, Icon } from 'antd';
import { url, tsid } from '../http';
import './image.css';

class Image extends React.Component {
    state = {
        loading: false,
    };

    upload = (uploader) => {
        console.log(uploader);
        let form = new FormData();
        form.append(this.props.name, uploader.file);
        let xhr = new XMLHttpRequest();
        xhr.open("POST", url('/tephra/ctrl-http/upload'));
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.setRequestHeader('tephra-session-id', tsid(), true);
        // xhr.onload = e => {
        //     console.log(e.currentTarget.response);
        //     console.log(xhr.responseText);
        // }
        xhr.onreadystatechange = e => {
            console.log(e.currentTarget.response);
            console.log(xhr.responseText);
        };
        xhr.send(form);
    }

    change = info => {
        if (info.file.status === 'uploading') {
            this.setState({ loading: true });

            return;
        }

        if (info.file.status === 'done') {
            this.setState({ loading: false });
            // Get this url from response in real world.
            // getBase64(info.file.originFileObj, imageUrl =>
            //     this.setState({
            //         imageUrl,
            //         loading: false,
            //     }),
            // );
        }
    };

    render = () => (
        <Upload
            name={this.props.name}
            listType="picture-card"
            className="image-uploader"
            showUploadList={false}
            customRequest={this.upload}
            // action={url('/tephra/ctrl-http/upload')}
            action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
            onChange={this.change}
        >
            {this.state.uri ? <img src={url(this.state.uri)} alt="upload" style={{ width: '100%' }} /> : <Icon type={this.state.loading ? 'loading' : 'plus'} />}
        </Upload>
    );
}

export default Image;