import React from 'react';
import { Form, Button } from 'antd'
import { service } from '../http';
import { BaseForm } from './form';

class SettingForm extends BaseForm {
    load = () => service('/classify/list', { code: this.props.meta.code }).then(data => {
        if (data === null) return null;

        let kvs = {};
        for (let classify of data) {
            kvs[classify.key] = classify.value;
        }

        return kvs;
    });

    toolbar = () => <Button onClick={this.submit}>保存</Button>;

    submit = (mt, values) => service(this.props.body.uri(this.props.uri, mt.service || mt.type), { ...values, ...this.props.parameter });
}

const Setting = Form.create({ name: 'setting-form' })(SettingForm);

export default Setting;