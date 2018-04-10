import * as React from 'react';
import generator from '../../util/generator';

export interface ComponentProps extends React.HTMLAttributes<HTMLElement> {
}

export class Component<T extends React.HTMLAttributes<HTMLElement>, E> extends React.Component<T, E> {
    private id: string;

    protected getId(prefix: string): string {
        if (!this.id)
            this.id = generator.random(32);

        return prefix + '-' + this.id;
    }

    protected getClassName(className: string): string {
        return '' + (this.props.className || className);
    }

    protected getDefaultValue(): string {
        if (!this.props.defaultValue)
            return '';

        if (typeof (this.props.defaultValue) === 'string')
            return this.props.defaultValue;

        return this.props.defaultValue.join(',');
    }
}