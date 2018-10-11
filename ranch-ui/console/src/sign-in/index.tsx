import * as React from 'react';
import { Layout, Form, Icon, Input, Button } from 'antd';
import { service } from '../service';
import { user, User } from '../user';
import './index.scss';

interface Props {
    sign(user: User): void;
}

export default class SginIn extends React.Component<Props, object> {
    public constructor(props: Props) {
        super(props);

        this.signIn = this.signIn.bind(this);
    }

    public render(): JSX.Element {
        const style = {
            color: 'rgba(0,0,0,.25)'
        };

        return (
            <Layout className="sign-in">
                <Form action="javascript:void(0);">
                    <Form.Item>
                        <Input prefix={<Icon type="user" style={style} />} name="uid" placeholder="用户名" autoFocus={true} />
                    </Form.Item>
                    <Form.Item>
                        <Input prefix={<Icon type="lock" style={style} />} name="password" type="password" placeholder="密码" onPressEnter={this.signIn} />
                    </Form.Item>
                    <Form.Item>
                        <Button type="primary" icon="login" onClick={this.signIn}>登入</Button>
                    </Form.Item>
                </Form>
            </Layout>
        );
    }

    private signIn(): void {
        const uid: HTMLInputElement | null = document.querySelector('input[name="uid"]');
        if (!uid) {
            return;
        }

        const password: HTMLInputElement | null = document.querySelector('input[name="password"]');
        if (!password) {
            return;
        }

        service.post({ uri: '/user/sign-in', parameter: { uid: uid.value, password: password.value } }).then(data => {
            if (data === null) {
                return;
            }

            user.set(data);
            this.props.sign(data);
        });
    }
}