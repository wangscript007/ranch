import * as React from 'react';
import { Layout, Form, Icon, Input, Button } from 'antd';
import { service } from '../service';
import { user, User } from '../user';
import './index.scss';

interface Props {
    sign(user: User): void;
}

interface State {
    signUp: boolean;
    type: string;
}

export default class Sgin extends React.Component<Props, State> {
    public constructor(props: Props) {
        super(props);

        this.state = {
            signUp: false,
            type: 'in'
        };

        this.submit = this.submit.bind(this);
        service.post({ uri: '/ui/console/sign-up' }).then(data => {
            if (data === null) {
                return;
            }

            this.setState({ signUp: data });
        });
    }

    public render(): JSX.Element {
        const style = {
            color: 'rgba(0,0,0,.25)'
        };

        return (
            <Layout className="sign">
                <Form action="javascript:void(0);">
                    <Form.Item>
                        <Input prefix={<Icon type="user" style={style} />} name="uid" placeholder="用户名" autoFocus={true} />
                    </Form.Item>
                    <Form.Item>
                        <Input prefix={<Icon type="lock" style={style} />} name="password" type="password" placeholder="密码" onPressEnter={this.submit} />
                    </Form.Item>
                    <Form.Item>
                        {this.state.type === 'in' ? <Button type="primary" icon="login" onClick={this.submit}>登入</Button> : null}
                        {this.state.type === 'up' ? <Button type="primary" icon="user-add" onClick={this.submit}>注册</Button> : null}
                    </Form.Item>
                    <Form.Item>
                        {this.state.signUp && this.state.type === 'in' ? <div className="up" onClick={this.change.bind(this, 'up')}>注册新账号</div> : null}
                        {this.state.type === 'up' ? <div className="in" onClick={this.change.bind(this, 'in')}>返回登录</div> : null}
                    </Form.Item>
                </Form>
            </Layout>
        );
    }

    private change(type: string): void {
        this.setState({ type: type });
    }

    private submit(): void {
        this.state.type === 'in' ? this.signIn() : this.signUp();
    }

    private signIn(): void {
        if (this.get('uid') === null) {
            return;
        }

        if (this.get('password') === null) {
            return;
        }

        service.post({ uri: '/user/sign-in', parameter: { uid: this.get('uid'), password: this.get('password') } }).then(data => {
            if (data === null) {
                return;
            }

            user.set(data);
            this.props.sign(data);
        });
    }

    private signUp(): void {
        if (this.get('uid') === null) {
            return;
        }

        if (this.get('password') === null) {
            return;
        }

        service.post({ uri: '/user/sign-up', parameter: { uid: this.get('uid'), password: this.get('password') } }).then(data => {
            if (data === null) {
                return;
            }

            user.set(data);
            this.props.sign(data);
        });
    }

    private get(name: string): string | null {
        const input: HTMLInputElement | null = document.querySelector('input[name="' + name + '"]');

        return input ? input.value : null;
    }
}