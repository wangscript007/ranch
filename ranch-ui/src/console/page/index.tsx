import * as React from 'react';
import selector from '../../util/selector';
import { Meta, Operate } from '../meta';
import { service, Success } from '../service';

export interface PageProps {
    meta: Meta;
    service: string;
    parameter: object;
    data: object;
    random: string;
}

export interface PageState {
    data: any;
}

export class PageComponent<T extends PageProps, E extends PageState> extends React.Component<T, E> {
    private random: string = "";

    protected refresh(): void {
        if (this.props.random === this.random)
            return;

        this.random = this.props.random;
        service.execute(this.props.service, {}, this.props.parameter).then(data => this.setState({ data: data }));
    }
}

interface ToolbarProps {
    meta: Meta;
    ops: Operate[];
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
        if (operate.type === 'create') {
            let parameter = service.getParameter(operate.parameter);
            service.to(this.props.meta.key + ".create", parameter, parameter);

            return;
        }

        if (operate.type === 'save') {
            let form: HTMLFormElement = selector.find('form.form');
            if (!form)
                return;

            let parameter = {};
            for (let i = 0; i < form.elements.length; i++) {
                let name = form.elements[i]['name'];
                if (name)
                    parameter[name] = form.elements[i]['value'];
            }
            service.execute(this.props.meta.key + '.save', {}, parameter, getSuccess(this.props.meta, operate, ".query"));

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