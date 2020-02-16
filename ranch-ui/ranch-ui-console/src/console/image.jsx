import React from 'react';
import { Upload, Icon } from 'antd';
import './image.css';

class Image extends React.Component {
    state = {
        loading: false,
    };

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
            name="avatar"
            listType="picture-card"
            className="image-uploader"
            showUploadList={false}
            action="https://www.mocky.io/v2/5cc8019d300000980a055e76"
            onChange={this.change}
        >
            {this.state.uri ? <img src={this.state.uri} alt="this.state.uri" style={{ width: '100%' }} /> : <Icon type={this.state.loading ? 'loading' : 'plus'} />}
        </Upload>
    );
}

export default Image;