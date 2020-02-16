import React from 'react';
import { Layout, Form, Input, Icon, Button } from 'antd';
import { service } from '../http';
import './index.css';

const { Footer, Content } = Layout;

class SignIn extends React.Component {
    constructor() {
        super();

        this.state = {
            up: false
        };
    }

    submit = e => {
        e.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (err) return;

            service('/user/sign-' + (this.state.up ? 'up' : 'in'), values).then(data => {
                if (data != null) {
                    window.location.reload();
                }
            });
        });
    };

    change = e => {
        e.preventDefault();
        this.setState({ up: !this.state.up });
    }

    render() {
        const { getFieldDecorator } = this.props.form;

        return (
            <Layout style={{ minHeight: '100vh' }}>
                <Content>
                    <div className="sign-in-header">Ranch UI Console</div>
                    <div className="sign-in-form">
                        <Form onSubmit={this.submit}>
                            <Form.Item>
                                {getFieldDecorator('uid', {
                                    rules: [{ required: true, message: '请输入用户名！' }],
                                })(
                                    <Input
                                        prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />}
                                        placeholder="用户名"
                                    />,
                                )}
                            </Form.Item>
                            <Form.Item>
                                {getFieldDecorator('password', {
                                    rules: [{ required: true, message: '请输入密码！' }],
                                })(
                                    <Input
                                        prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />}
                                        type="password"
                                        placeholder="密码"
                                    />,
                                )}
                            </Form.Item>
                            <Form.Item>
                                <Button type="primary" htmlType="submit" className="sign-in-button">{this.state.up ? '注册' : '登入'}</Button>
                            </Form.Item>
                            <Form.Item>
                                <Button className="sign-in-button" onClick={this.change}>{this.state.up ? '登录' : '注册'}</Button>
                            </Form.Item>
                        </Form>
                    </div>
                </Content>
                <Footer className="sign-in-footer">Copyright &copy; {new Date().getFullYear()} ranch-ui-console</Footer>
            </Layout>
        );
    }
}

export default Form.create({ name: 'sign-in' })(SignIn);