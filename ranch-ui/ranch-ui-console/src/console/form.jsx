import React from 'react';
import { Form, Radio, Select, DatePicker, Input, Button } from 'antd';
import moment from 'moment';
import { service } from '../http';
import meta from './meta';
import { Image, getImageUri } from './image';
import './form.css';

const { Option } = Select;
const { TextArea } = Input;

class FieldForm extends React.Component {
    constructor(props) {
        super(props);

        this.state = {};
        if (props.uri && !props.data) {
            service(this.props.body.uri(this.props.uri, props.meta.service), props.parameter).then(data => {
                if (data === null) return;

                this.data = data;
                const { getFieldsValue, setFieldsValue } = this.props.form;
                let values = getFieldsValue();
                for (let key in values) {
                    values[key] = data[key];
                }
                for (let column of meta.props(this.props.columns, this.props.meta.columns)) {
                    let value = values[column.name];
                    if (!value) continue;

                    if (column.labels)
                        values[column.name] = '' + value;
                    else if (column.type === 'date')
                        values[column.name] = moment(value, 'YYYY-MM-DD');
                    else if (column.type === 'datetime')
                        values[column.name] = moment(value, 'YYYY-MM-DD HH:mm:ss');
                }
                setFieldsValue(values);
            });
        }
    }

    button = (mt) => {
        const { getFieldsValue } = this.props.form;
        let values = getFieldsValue();
        if (this.props.data && this.props.data.id) values.id = this.props.data.id;
        for (let column of meta.props(this.props.columns, this.props.meta.columns)) {
            if (column.type === 'image') {
                values[column.name] = getImageUri(column.upload);

                continue;
            }

            let value = values[column.name];
            if (!value) {
                delete values[column.name];

                continue;
            };

            if (column.type === 'date')
                values[column.name] = value.format("YYYY-MM-DD");
            else if (column.type === 'datetime')
                values[column.name] = value.format("YYYY-MM-DD HH:mm:ss");
        }
        service(this.props.body.uri(this.props.uri, mt.service || mt.type), { ...values, ...this.props.parameter }).then(data => {
            if (data === null || !mt.success) return;

            this.props.body.load(this.props.body.uri(this.props.uri, mt.success), this.props.parameter);
        });
    }

    render = () => {
        const { getFieldDecorator } = this.props.form;
        let items = [];
        let data = this.data || this.props.data || {};
        for (let column of meta.props(this.props.columns, this.props.meta.columns)) {
            let element = '';
            let value = data[column.name];
            if (column.type === 'read-only')
                element = value || '';
            else if (column.type === 'image') {
                element = <Image upload={column.upload} size={column.size || 1} value={value} />;
            } else {
                let option = {};
                if (column.type === 'date') {
                    if (value) option.initialValue = moment(value, 'YYYY-MM-DD');
                } else if (column.type === 'datetime') {
                    if (value) option.initialValue = moment(value, 'YYYY-MM-DD HH:mm:ss');
                } else {
                    option.initialValue = value || '';
                }
                element = getFieldDecorator(column.name, option)(this.input(column))
            }
            items.push(<Form.Item key={column.name} className={'console-form-item console-form-item-' + (items.length % 2 === 0 ? 'even' : 'odd')} label={column.label}>{element}</Form.Item>);
        }
        let buttons = [];
        if (this.props.meta.toolbar) {
            for (let toolbar of this.props.meta.toolbar) {
                buttons.push(<Button key={toolbar.label} onClick={this.button.bind(this, toolbar)}>{toolbar.label}</Button>);
            }
        }

        return (
            <Form labelCol={{ xs: { span: 24 }, sm: { span: 6 } }} wrapperCol={{ xs: { span: 24 }, sm: { span: 12 }, }}>
                {items}
                <Form.Item className="console-form-toolbar" label="toolbar">{buttons}</Form.Item>
            </Form>
        );
    }

    input = column => {
        if (column.labels) {
            if (column.labels.length < 5) {
                let radios = [];
                for (let index in column.labels) {
                    radios.push(<Radio key={index} value={index}>{column.labels[index]}</Radio>);
                }

                return <Radio.Group>{radios}</Radio.Group>;
            }

            let options = [];
            for (let index in column.labels) {
                options.push(<Option key={index} value={index}>{column.labels[index]}</Option>);
            }

            return <Select>{options}</Select>
        }

        if (column.type === 'date') return <DatePicker />;

        if (column.type === 'datetime') return <DatePicker showTime={true} />;

        if (column.type === 'text-area') return <TextArea />;

        return <Input />
    }
}

const WrappedFieldForm = Form.create({ name: 'form' })(FieldForm);

export default WrappedFieldForm;