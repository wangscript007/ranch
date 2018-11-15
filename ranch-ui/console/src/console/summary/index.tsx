import * as React from 'react';
import { Alert } from 'antd';
import { pager } from '../pager';
import './index.scss';

interface Props {
    service: string;
}

interface State {
    list: Array<{
        label: string;
        value: string;
    }>;
}

export default class Summary extends React.Component<Props, State>{
    constructor(props: Props) {
        super(props);

        this.state = {
            list: []
        };

        pager.post({ service: this.props.service }).then(data => {
            if (data === null || data.length === 0) {
                return;
            }

            this.setState({
                list: data
            });
        });
    }

    public render(): JSX.Element | null {
        if (!this.state.list || this.state.list.length === 0) {
            return null;
        }

        return (
            <Alert type="info" className="summaries" message={this.state.list.map((obj, i) =>
                <div key={i} className="summary">
                    <div className="summary-label">{obj.label}</div>
                    <div className="summary-value">{obj.value}</div>
                </div>
            )} />
        );
    }
}