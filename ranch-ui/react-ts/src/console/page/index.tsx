import * as React from 'react';
import selector from '../../util/selector';
import merger from '../../util/merger';
import message from '../../util/message';
import Date from '../../ui/date';
import Image from '../../ui/image';
import Base64 from '../../ui/base64';
import Editor from '../../ui/editor';
import Map from '../../ui/map';
import { Meta, Prop, Operate } from '../meta';
import { service, Success } from '../service';

export interface PageProps {
    meta: Meta;
    service: string;
    parameter: object;
    data: object;
}

export interface PageState {
    data: any;
}

export class PageComponent<T extends PageProps, E extends PageState> extends React.Component<T, E> {
    protected findProp(props: Prop[], prop: Prop): Prop {
        for (let i = 0; i < props.length; i++)
            if (props[i].name === prop.name)
                return merger.merge<Prop>({ name: '', label: '' }, props[i], prop);

        return prop;
    }

    protected input(prop: Prop, data: object, selectAll?: string): JSX.Element | string {
        let value: string = this.findValue(prop, data);
        if (prop.type === 'read-only')
            return value;

        let props = {
            name: prop.name,
            defaultValue: value
        };
        if (prop.type === 'text-area')
            return <textarea {...props} />

        if (prop.type === 'date')
            return <Date {...props} />

        if (prop.type === 'image')
            return <Image {...props} fieldName={prop['fieldName'] || this.props.meta.key + '.' + prop.name} />

        if (prop.type === 'base64')
            return <Base64 {...props} />

        if (prop.type === 'editor')
            return <Editor {...props} />

        if (prop.type === 'map')
            return <Map {...props} address={true} change={true} />

        if (prop.labels && prop.labels.length > 0) {
            return (
                <select {...props} >
                    {selectAll ? <option value="">{message.get(selectAll)}</option> : null}
                    {prop.labels.map((label, index) => <option key={index} value={index}>{label}</option>)}
                </select>
            );
        }

        if (prop.values) {
            let options: JSX.Element[] = [];
            if (selectAll)
                options.push(<option value="">{message.get(selectAll)}</option>);
            for (let value in prop.values)
                options.push(<option value={value} key={value}>{prop.values[value]}</option>);

            return <select {...props}>{options}</select>;
        }

        return <input type={prop.type || 'text'} {...props} />;
    }

    protected ignore(prop: Prop): boolean {
        if (!prop.ignore)
            return false;

        for (let i = 0; i < prop.ignore.length; i++)
            if (this.props.service === this.props.meta.key + '.' + prop.ignore[i])
                return true;

        return false;
    }

    protected findValue(prop: Prop, data: object): string {
        if (!data || !data.hasOwnProperty(prop.name))
            return '';

        let value = data[prop.name];
        if (typeof (value) !== 'object')
            return value;

        if (prop.valueKey && value.hasOwnProperty(prop.valueKey))
            return value[prop.valueKey];

        return JSON.stringify(value);
    }
}

interface ToolbarProps {
    meta: Meta;
    ops?: Operate[];
    form?: string;
}

export class Toolbar extends React.Component<ToolbarProps, object> {
    render(): JSX.Element | null {
        if (!this.props.ops || this.props.ops.length == 0)
            return null;

        return (
            <div className="toolbar">
                {this.props.ops.map((operate, index) => <button key={index} onClick={() => this.click(operate)}>{operate.label}</button>)}
            </div>
        );
    }

    private click(operate: Operate): void {
        if (operate.type === 'create' || operate.type === 'lite-create') {
            let parameter = service.getParameter(operate.parameter);
            service.to(this.props.meta.key + '.' + operate.type, parameter, parameter);

            return;
        }

        if (operate.type === 'save') {
            if (!this.props.form)
                return;

            let form: HTMLFormElement = selector.find(this.props.form);
            if (!form)
                return;

            let parameter = {};
            for (let i = 0; i < form.elements.length; i++) {
                let name = form.elements[i]['name'];
                if (name)
                    parameter[name] = form.elements[i]['value'];
            }
            service.execute(this.props.meta.key + '.save', {}, service.getParameter(parameter), getSuccess(this.props.meta, operate, ".query"));

            return;
        }
    }
}

export function getSuccess(meta: Meta, operate: Operate, defaultValue: string): Success {
    let success = operate.success || defaultValue;
    if (success.charAt(0) === '.')
        success = meta.key + success;

    return {
        service: success,
        parameter: service.getParameter(operate.parameter)
    };
}