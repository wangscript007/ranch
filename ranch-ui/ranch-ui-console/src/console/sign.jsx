import React from 'react';
import { Spin, Avatar, Icon } from 'antd';
import { post, url, loader } from '../http';
import './sign.css';

class Sign extends React.Component {
    constructor(props) {
        super(props);

        this.state = { loading: false };
        loader(this);
    }
    
    sign = () => {
        this.props.body.load('/user/sign', {}, null);
    }

    signOut = () => {
        post('/user/sign-out').then(json => window.location.reload());
    }

    render = () => {
        let nick = this.props.user.nick || 'Ranch UI';

        return (
            <div className="console-sign">
                <div className="console-sign-loading"><Spin spinning={this.state.loading} /></div>
                <div className="console-sign-avatar" onClick={this.sign}>
                    {this.props.user.portrait ? <Avatar src={url(this.props.user.portrait)} /> : <Avatar>{nick.substring(0, 1)}</Avatar>}
                    <span>{nick}</span>
                </div>
                <div className="console-sign-out" onClick={this.signOut}>
                    <Icon type="logout" />
                    <span>退出</span>
                </div>
            </div>
        );
    }
}

export default Sign;