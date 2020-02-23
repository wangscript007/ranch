import React from 'react';
import { Form, Radio, Select, DatePicker, Input, Button } from 'antd';
import moment from 'moment';
import { service } from '../http';
import meta from './meta';
import Image from './image';
import Editor from './editor';
import './form.css';

const { Option } = Select;
const { TextArea } = Input;

class BaseForm extends React.Component {
    state = {};
    values = {};

    componentDidMount = () => {
        if (this.props.uri && !this.props.data) {
            this.load().then(data => {
                if (data === null) return;

                this.data = data;
                const { getFieldsValue, setFieldsValue } = this.props.form;
                let values = getFieldsValue();
                for (let key in values) {
                    values[key] = data[key];
                }
                for (let prop of meta.props(this.props.props, this.props.meta.props)) {
                    let value = values[prop.name];
                    if (!value) continue;

                    if (prop.labels)
                        values[prop.name] = '' + value;
                    else if (prop.type === 'date')
                        values[prop.name] = moment(value, 'YYYY-MM-DD');
                    else if (prop.type === 'datetime')
                        values[prop.name] = moment(value, 'YYYY-MM-DD HH:mm:ss');
                }
                setFieldsValue(values);
            });
        }
    }

    value = (name, value) => this.values[name] = value;

    button = mt => {
        const { getFieldsValue } = this.props.form;
        let values = getFieldsValue();
        if (this.props.data && this.props.data.id) values.id = this.props.data.id;
        for (let prop of meta.props(this.props.props, this.props.meta.props)) {
            let value = values[prop.name];
            if (!value) {
                delete values[prop.name];

                continue;
            };

            if (prop.type === 'date')
                values[prop.name] = value.format("YYYY-MM-DD");
            else if (prop.type === 'datetime')
                values[prop.name] = value.format("YYYY-MM-DD HH:mm:ss");
        }
        this.submit(mt, { ...values, ...this.values }).then(data => {
            if (data === null || !mt.success) return;

            this.props.body.load(this.props.body.uri(this.props.uri, mt.success), this.props.parameter);
        });
    }

    cancel = mt => {
        this.props.body.load(this.props.body.uri(this.props.uri, mt.success), this.props.parameter);
    }

    render = () => {
        const { getFieldDecorator } = this.props.form;
        let items = [];
        let data = this.data || this.props.data || {};
        for (let prop of meta.props(this.props.props, this.props.meta.props)) {
            let element = '';
            let value = data[prop.name];
            if (prop.type === 'read-only')
                element = value || '';
            else if (prop.type === 'image') {
                element = <Image name={prop.name} upload={prop.upload} size={prop.size || 1} value={value} form={this} />;
            } else if (prop.type === 'editor') {
                element = <Editor name={prop.name} value={value || ''} form={this} />;
            } else {
                let option = {};
                if (prop.type === 'date') {
                    if (value) option.initialValue = moment(value, 'YYYY-MM-DD');
                } else if (prop.type === 'datetime') {
                    if (value) option.initialValue = moment(value, 'YYYY-MM-DD HH:mm:ss');
                } else {
                    option.initialValue = value || '';
                }
                element = getFieldDecorator(prop.name, option)(this.input(prop))
            }
            items.push(<Form.Item key={prop.name} className={'console-form-item console-form-item-' + (items.length % 2 === 0 ? 'even' : 'odd')} label={prop.label}>{element}</Form.Item>);
        }

        return (
            <Form labelCol={{ xs: { span: 24 }, sm: { span: 6 } }} wrapperCol={{ xs: { span: 24 }, sm: { span: 12 }, }}>
                {items}
                <Form.Item className="console-form-toolbar" label="toolbar">{this.toolbar()}</Form.Item>
            </Form>
        );
    }

    input = prop => {
        if (prop.labels) {
            if (prop.labels.length < 5) {
                let radios = [];
                for (let index in prop.labels) {
                    radios.push(<Radio key={index} value={index}>{prop.labels[index]}</Radio>);
                }

                return <Radio.Group>{radios}</Radio.Group>;
            }

            let options = [];
            for (let index in prop.labels) {
                options.push(<Option key={index} value={index}>{prop.labels[index]}</Option>);
            }

            return <Select>{options}</Select>
        }

        if (prop.type === 'date') return <DatePicker />;

        if (prop.type === 'datetime') return <DatePicker showTime={true} />;

        if (prop.type === 'text-area') return <TextArea />;

        return <Input />
    }
}

class NormalForm extends BaseForm {
    load = () => service(this.props.body.uri(this.props.uri, this.props.meta.service), this.props.parameter);

    toolbar = () => {
        let buttons = [];
        if (this.props.meta.toolbar) {
            for (let toolbar of this.props.meta.toolbar) {
                buttons.push(<Button key={toolbar.label} onClick={this.button.bind(this, toolbar)}>{toolbar.label}</Button>);
                if (toolbar.success && buttons.length === this.props.meta.toolbar.length)
                    buttons.push(<Button key="cancel" type="dashed" onClick={this.cancel.bind(this, toolbar)}>取消</Button>);
            }
        }

        return buttons;
    }

    submit = (mt, values) => service(this.props.body.uri(this.props.uri, mt.service || mt.type), { ...values, ...this.props.parameter });
}

const Normal = Form.create({ name: 'normal-form' })(NormalForm);

export default Normal;

export { BaseForm };