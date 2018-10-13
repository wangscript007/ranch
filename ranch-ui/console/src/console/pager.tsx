import * as React from 'react';
import { Input, Radio, Select, DatePicker, InputNumber } from 'antd';
import moment from 'moment';
import merger from '../util/merger';
import { service } from '../service';
import { meta, PropMeta, PageMeta, ActionMeta } from './meta';
import { Page } from './page';
import { Image, getImageValue } from './ui/image';
import { Attachment, getAttachmentValue } from './ui/attachment';
import Remote from './ui/remote';
import http from '../util/http';

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

    public dashboard(): void {
        this.page.setState({ service: 'blank' }, () => {
            this.page.setState({
                service: '',
                meta: {
                    type: ''
                }
            });
        });
    }

    private getProps(name: string): PropMeta[] {
        const array: PropMeta[] = [];
        for (const prop of meta.now().props) {
            if (this.ignore(prop, name)) {
                continue;
            }

            array.push(prop);
        }

        return array;
    }

    private ignore(prop: PropMeta, name: string): boolean {
        if (prop.hasOwnProperty('show-only')) {
            for (const showonly of prop['show-only']) {
                if (showonly === name) {
                    return false;
                }
            }

            return true;
        }

        return this.has(prop, 'ignore', name);
    }

    private has(prop: PropMeta, key: string, name: string): boolean {
        if (!prop.hasOwnProperty(key)) {
            return false;
        }

        for (const value of prop[key]) {
            if (value === name) {
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

        if (prop.type === 'read-only' || this.has(prop, 'read-only', meta.nowService())) {
            if (prop.labels) {
                return <Input readOnly={true} value={prop.labels[config.initialValue || 0]} />;
            }

            if (prop.values) {
                return <Input readOnly={true} value={prop.values[config.initialValue]} />;
            }

            if (prop.type === 'image') {
                return <a className="read-only-image" href={http.url(config.initialValue)} target="_blank"><img src={http.url(config.initialValue)} /></a>
            }

            return <Input readOnly={true} value={config.initialValue} />;
        }

        if (prop.remote) {
            return <Remote {...prop.remote} getFieldDecorator={getFieldDecorator(prop.name, config)} />;
        }

        return getFieldDecorator(prop.name, config)(this.getInputElement(prop, search));
    }

    private getInputElement(prop: PropMeta, search?: boolean): JSX.Element {
        if (prop.labels) {
            if (!search && prop.labels.length <= 3) {
                return (
                    <Radio.Group>
                        {prop.labels.map((label, i) => <Radio key={i} value={i}>{label}</Radio>)}
                    </Radio.Group>
                );
            }

            return (
                <Select style={{ minWidth: 200 }}>
                    {search ? <Select.Option value="">全部</Select.Option> : null}
                    {prop.labels.map((label, i) => <Select.Option key={i} value={i}>{label}</Select.Option>)}
                </Select>
            );
        }

        if (prop.values) {
            const keys = Object.keys(prop.values);
            if (!search && keys.length <= 3) {
                return (
                    <Radio.Group>
                        {keys.map((key) => <Radio key={key} value={key}>{(prop.values || {})[key]}</Radio>)}
                    </Radio.Group>
                );
            }

            return (
                <Select style={{ minWidth: 200 }}>
                    {search ? <Select.Option value="">全部</Select.Option> : null}
                    {keys.map((key) => <Select.Option key={key} value={key}>{(prop.values || {})[key]}</Select.Option>)}
                </Select>
            );
        }

        if (prop.remote) {
            return <Remote {...prop.remote} />
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
                return <Input.TextArea autosize={{ minRows: 4, maxRows: 16 }} />;
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