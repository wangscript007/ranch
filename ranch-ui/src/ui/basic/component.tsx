import * as React from 'react';
import generator from '../../util/generator';

export interface ComponentProps extends React.HTMLAttributes<any> {
}

export class Component<T extends ComponentProps, E> extends React.Component<T, E> {
    private id: string;

    protected getId(prefix: string): string {
        if (!this.id)
            this.id = generator.random(32);

        return prefix + '-' + this.id;
    }

    protected getClassName(className: string): string {
        return '' + (this.props.className || className);
    }
}