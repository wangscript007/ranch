import * as React from 'react';
import { Layout, Form, Icon, Input, Button } from 'antd';
import { service, User } from '../service';
import './index.scss';

const { Content } = Layout;
const FormItem = Form.Item;

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
                <Content>
                    <Form action="javascript:void(0);">
                        <FormItem>
                            <Input prefix={<Icon type="user" style={style} />} name="uid" placeholder="用户名" autoFocus={true} />
                        </FormItem>
                        <FormItem>
                            <Input prefix={<Icon type="lock" style={style} />} name="password" type="password" placeholder="密码" onPressEnter={this.signIn} />
                        </FormItem>
                        <FormItem>
                            <Button type="primary" icon="login" onClick={this.signIn}>登入</Button>
                        </FormItem>
                    </Form>
                </Content>
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

        service.post('/user/sign-in', {}, { uid: uid.value, password: password.value }).then(data => {
            if (data === null) {
                return;
            }

            this.props.sign(data);
        });
    }
}