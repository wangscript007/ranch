import * as React from 'react';
import { Input, Radio, Select, DatePicker, InputNumber } from 'antd';
import moment from 'moment';
import merger from '../util/merger';
import { service } from '../service';
import { meta, PropMeta, PageMeta, ActionMeta } from './meta';
import { Page } from './page';
import { Image, getImageValue } from './ui/image';
import { Attachment, getAttachmentValue } from './ui/attachment';

export interface Request {
    service: string;
    meta?: string;
    header?: object;
    parameter?: object;
}

export interface Model {
    id?: string;
}

const icons = {
    'search': 'search',
    'create': 'plus',
    'modify': 'edit',
    'save': 'save',
    'copy': 'copy',
    'delete': 'delete'
};

const RadioGroup = Radio.Group;
const Option = Select.Option;
const TextArea = Input.TextArea;
const DateFormat = 'YYYY-MM-DD';

class Pager {
    private page: Page;

    public bind(page: Page): void {
        this.page = page;
    }

    public to(request: Request): void {
        meta.get(request.service).then(mt => {
            if (mt === null) {
                return;
            }

            this.post(request).then(data => {
                if (data === null) {
                    return;
                }

                this.setPage({
                    service: request.service,
                    data: data,
                    header: request.header,
                    parameter: request.parameter
                });
            });
        });
    }

    public setPage(page: { service: string, data: any, header?: object, parameter?: object }): void {
        const name: string = page.service.substring(page.service.lastIndexOf(page.service.indexOf('.') === -1 ? '/' : '.') + 1);
        const pageMeta: PageMeta = meta.now()[name];
        this.toolbar(pageMeta.toolbar);
        this.page.setState({ service: 'blank' }, () => {
            this.page.setState({
                service: page.service,
                header: page.header,
                parameter: page.parameter,
                meta: pageMeta,
                props: this.getProps(name),
                data: page.data
            });
        });
    }

    private getProps(name: string): PropMeta[] {
        const array: PropMeta[] = [];
        for (const prop of meta.now().props) {
            if (this.ignore(prop, name)) {
                continue;
            }

            if (this.readonly(prop, name)) {
                prop.readonly = true;
            }
            array.push(prop);
        }

        return array;
    }

    private ignore(prop: PropMeta, name: string): boolean {
        if (!prop.ignore || prop.ignore.length === 0) {
            return false;
        }

        for (const ignore of prop.ignore) {
            if (ignore === name) {
                return true;
            }
        }

        return false;
    }

    private readonly(prop: PropMeta, name: string): boolean {
        if (!prop.hasOwnProperty('read-only')) {
            return false;
        }

        const readonly: string[] = prop['read-only'];
        if (readonly.length === 0) {
            return false;
        }

        for (const ro of readonly) {
            if (ro === name) {
                return true;
            }
        }

        return false;
    }

    private toolbar(toolbar?: ActionMeta[]): void {
        if (!toolbar) {
            return;
        }

        for (const button of toolbar) {
            button.icon = this.getButtonIcon(button.type, button.icon);
        }
    }

    private getButtonIcon(type: string, icon?: string): string | undefined {
        if (icon) {
            return icon;
        }

        for (const key in icons) {
            if (icons.hasOwnProperty(key) && type.endsWith(key)) {
                return icons[key];
            }
        }

        return undefined;
    }

    public getInput(form: any, prop: PropMeta, data?: {}, search?: boolean): JSX.Element {
        const { getFieldDecorator } = form;
        const config = {
            initialValue: this.getModelValue(data, prop.name)
        };
        if (prop.labels && config.initialValue && typeof config.initialValue === 'string') {
            config.initialValue = parseInt(config.initialValue, 10);
        } else if (prop.type === 'date') {
            if (config.initialValue === '') {
                delete config.initialValue;
            } else {
                config.initialValue = moment(config.initialValue, DateFormat);
            }
        } else if (prop.type === 'money') {
            config.initialValue = (config.initialValue * 0.01).toFixed(2);
        }

        if (prop.readonly || prop.type === 'read-only') {
            if (prop.labels) {
                return <Input readOnly={true} value={prop.labels[config.initialValue || 0]} />;
            }

            if (prop.values) {
                return <Input readOnly={true} value={prop.values[config.initialValue]} />;
            }

            return <Input readOnly={true} value={config.initialValue} />;
        }

        return getFieldDecorator(prop.name, config)(this.getInputElement(prop, search));
    }

    private getInputElement(prop: PropMeta, search?: boolean): JSX.Element {
        if (prop.labels) {
            if (!search && prop.labels.length <= 3) {
                return (
                    <RadioGroup>
                        {prop.labels.map((label, i) => <Radio key={i} value={i}>{label}</Radio>)}
                    </RadioGroup>
                );
            }

            return (
                <Select style={{ minWidth: 200 }}>
                    {search ? <Option value="">全部</Option> : null}
                    {prop.labels.map((label, i) => <Option key={i} value={i}>{label}</Option>)}
                </Select>
            );
        }

        if (prop.values) {
            const keys = Object.keys(prop.values);
            if (!search && keys.length <= 3) {
                return (
                    <RadioGroup>
                        {keys.map((key) => <Radio key={key} value={key}>{(prop.values || {})[key]}</Radio>)}
                    </RadioGroup>
                );
            }

            return (
                <Select style={{ minWidth: 200 }}>
                    {search ? <Option value="">全部</Option> : null}
                    {keys.map((key) => <Option key={key} value={key}>{(prop.values || {})[key]}</Option>)}
                </Select>
            );
        }

        switch (prop.type) {
            case 'password':
                return <Input type="password" />;
            case 'date':
                return <DatePicker format={DateFormat} />;
            case 'date-range':
                return <DatePicker.RangePicker format={DateFormat} />;
            case 'number':
                return <InputNumber />;
            case 'text-area':
                return <TextArea autosize={{ minRows: 4, maxRows: 16 }} />;
            case 'image':
                return <Image name={prop.name} upload={prop.upload || (meta.now().key + '.' + prop.name)} />;
            case 'attachment':
                return <Attachment name={prop.name} upload={prop.upload || (meta.now().key + '.' + prop.name)} />;
            default:
                return <Input />;
        }
    }

    public getModelValue(model?: Model, name?: string): any {
        if (!model || !name) {
            return '';
        }

        if (model.hasOwnProperty(name)) {
            return model[name];
        }

        if (name.indexOf('.') === -1) {
            return '';
        }

        let obj: any = model;
        for (const n of name.split('.')) {
            if (!obj || !obj.hasOwnProperty(n)) {
                return '';
            }

            obj = obj[n];
        }

        return obj;
    }

    public getFormValue(form: any, props: PropMeta[]): {} {
        const obj = {};
        const { getFieldValue } = form;
        props.map(prop => {
            if (prop.type === 'read-only') {
                return;
            }

            if (prop.type === 'image') {
                obj[prop.name] = getImageValue(prop.name);

                return;
            }

            if (prop.type === 'attachment') {
                obj[prop.name] = getAttachmentValue(prop.name);

                return;
            }

            const value: any = getFieldValue(prop.name);
            if (value === null || value === undefined) {
                return;
            }

            obj[prop.name] = value;
            if (prop.type === 'date') {
                obj[prop.name] = value.format(DateFormat);
            } else if (prop.type === 'date-range') {
                obj[prop.name] = value.length === 0 ? '' : (value[0].format(DateFormat) + ',' + value[1].format(DateFormat));
            } else if (prop.type === 'money') {
                obj[prop.name] = Math.round(value * 100);
            }
        });

        return obj;
    }

    public getService(key: string, action: ActionMeta, service?: string): string {
        const k = service || action.service || ((key.charAt(0) === '/' ? '#' : '.') + action.type);
        if (k.charAt(0) === '.') {
            return key.substring(0, key.lastIndexOf('.')) + k;
        }

        if (k.charAt(0) === '#') {
            return key.substring(0, key.lastIndexOf('/') + 1) + k.substring(1);
        }

        return k;
    }

    public success(key: string, action: ActionMeta, header?: {}, parameter?: {}, model?: {}): void {
        if (!action.success) {
            return;
        }

        if (typeof action.success === 'string') {
            pager.to({
                service: this.getService(key, action, action.success),
                header: header,
                parameter: parameter
            });

            return;
        }

        const param = parameter || {};
        if (action.success.parameter) {
            if (!model) {
                model = {};
            }
            for (const name in action.success.parameter) {
                if (action.success.parameter.hasOwnProperty(name)) {
                    param[name] = model[name] || action.success.parameter[name];
                }
            }
        }

        pager.to({
            service: this.getService(key, action, action.success.service),
            header: merger.merge(header || {}, action.success.header || {}),
            parameter: param
        });
    }

    public post(request: Request): Promise<any> {
        if (request.service.charAt(0) === '/') {
            return service.post({
                uri: request.service,
                header: request.header,
                parameter: request.parameter
            });
        }

        return service.post({
            uri: '/ui/console/service',
            header: merger.merge({}, request.header || {}, { key: request.service }),
            parameter: request.parameter
        });
    }
}

export const pager = new Pager();