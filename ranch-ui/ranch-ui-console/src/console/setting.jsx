import React from 'react';
import { Form, Button } from 'antd'
import { service } from '../http';
import { BaseForm } from './form';

class SettingForm extends BaseForm {
    load = () => service('/classify/list', { code: this.code() }).then(data => {
        if (data === null) return null;

        let kvs = {};
        for (let classify of data) {
            kvs[classify.key] = classify.value;
        }

        return kvs;
    });

    toolbar = () => <Button onClick={this.button.bind(this, this.props.meta)}>保存</Button>;

    submit = (mt, values) => {
        let array = [];
        let code = this.code();
        for (let prop of mt.props) {
            array.push({
                code: code,
                key: prop.name,
                value: values[prop.name] || ''
            });
        }

        return service('/classify/saves', { classifies: JSON.stringify(array) });
    }

    code = () => this.props.uri.substring(1).replace(/\//g, '.');
}

const Setting = Form.create({ name: 'setting-form' })(SettingForm);

export default Setting;