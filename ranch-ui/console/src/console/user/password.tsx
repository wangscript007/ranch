import * as React from 'react';
import { Form, Button } from 'antd';
import { user } from '../../user';
import { ActionMeta } from '../meta';
import { PageState } from '../page';
import { pager } from '../pager';

interface Props extends PageState {
    form: any;
}

class Password extends React.Component<Props>{
    public constructor(props: Props) {
        super(props);
    }

    public render(): JSX.Element {
        const formItemLayout = {
            labelCol: {
                xs: { span: 24 },
                sm: { span: 8 },
            },
            wrapperCol: {
                xs: { span: 24 },
                sm: { span: 16 },
            },
        };

        return (
            <Form>
                <Form.Item label="旧密码" {...formItemLayout}>
                    {pager.getInput(this.props.form, {
                        name: 'old',
                        type: 'password'
                    }, {})}
                </Form.Item>
                <Form.Item label="新密码" {...formItemLayout}>
                    {pager.getInput(this.props.form, {
                        name: 'new',
                        type: 'password'
                    }, {})}
                </Form.Item>
                <Form.Item label="重复新密码" {...formItemLayout}>
                    {pager.getInput(this.props.form, {
                        name: 'repeat',
                        type: 'password'
                    }, {})}
                </Form.Item>
                <Form.Item
                    wrapperCol={{
                        xs: { span: 24, offset: 0 },
                        sm: { span: 16, offset: 8 },
                    }}
                >
                    {this.props.meta.toolbar ? this.props.meta.toolbar.map((button) =>
                        <Button key={button.type} type="primary" icon={button.icon} onClick={this.click.bind(this, button)}>{button.label}</Button>
                    ) : null}
                </Form.Item>
            </Form>
        );
    }

    private click(action: ActionMeta): void {
        pager.post({
            service: '/user/password',
            parameter: pager.getFormValue(this.props.form, [{
                name: 'old'
            },
            {
                name: 'new'
            }, {
                name: 'repeat'
            }])
        }).then(data => {
            if (data === null) {
                return;
            }

            user.out();
        });
    }
}

export default Form.create()(Password);