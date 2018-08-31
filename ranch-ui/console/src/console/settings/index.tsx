import * as React from 'react';
import { Form, Button } from 'antd';
import { pager, Action } from '../pager';
import { PageState } from '../page';

interface Props extends PageState {
    form: any;
}

class Settings extends React.Component<Props> {
    public constructor(props: Props) {
        super(props);

        pager.post('ranch.classify.list', {}, { code: "settings" }).then(data => {
            if (data === null) {
                return;
            }

            const obj = {};
            for (const classify of data) {
                obj[classify.key] = classify.value;
            }
            this.setState(obj);
        });
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
                {this.props.props.map((prop, i) =>
                    <Form.Item key={i} label={prop.label} {...formItemLayout}>
                        {pager.getInput(this.props.form, prop, this.state)}
                    </Form.Item>
                )}
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

    private click(action: Action): void {
        const array: Array<{}> = [];
        const obj = pager.getFormValue(this.props.form, this.props.props);
        this.props.props.map(prop => {
            if (!obj.hasOwnProperty(prop.name)) {
                return;
            }

            array.push({
                code: 'settings',
                key: prop.name,
                value: obj[prop.name],
                name: prop.label
            });
        });
        pager.post('ranch.classify.saves', {}, {
            classifies: JSON.stringify(array)
        });
    }
}

export default Form.create()(Settings);