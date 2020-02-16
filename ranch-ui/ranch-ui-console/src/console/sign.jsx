import React from 'react';
import { Avatar, Icon } from 'antd';
import { post } from '../http';
import './sign.css';

class Sign extends React.Component {
    sign = () => {
        this.props.body.load('/user/sign', {}, null);
    }

    signOut = () => {
        post('/user/sign-out').then(json => window.location.reload());
    }

    render = () => {
        let nick = this.props.user.nick || 'Ranch';

        return (
            <div className="console-sign">
                <div className="console-sign-avatar" onClick={this.sign}>
                    {this.props.user.portrait ? <Avatar src={this.props.user.portrait} /> : <Avatar>{nick.substring(0, 1)}</Avatar>}
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