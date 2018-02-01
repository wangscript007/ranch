import * as React from 'react';
import { ComponentProps, Component } from '../basic/component';

declare global {
    interface Window { UM: any; }
}

interface Props extends ComponentProps {
    name: string;
}

export default class Editor extends Component<Props, object> {
    private editorId: string;

    constructor(props: Props) {
        super(props);

        this.editorId = this.getId('editor');
    }

    componentDidMount(): void {
        window.UM.getEditor(this.editorId, {
            toolbar: [
                'source | undo redo | bold italic underline strikethrough | superscript subscript | forecolor backcolor',
                '| insertorderedlist insertunorderedlist | removeformat selectall cleardoc paragraph fontsize',
                '| justifyleft justifycenter justifyright justifyjustify | link unlink | horizontal'
            ]
        });
    }

    componentWillUnmount(): void {
        window.UM.delEditor(this.editorId);
    }

    render(): JSX.Element {
        let props = {
            type: 'text/plain',
            id: this.editorId,
            name: this.props.name,
            style: {
                width: '100%'
            }
        }

        return (
            <script { ...props }>{this.props.defaultValue}</script>
        );
    }
}