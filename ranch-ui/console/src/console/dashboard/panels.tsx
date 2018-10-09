import * as React from 'react';
import { Row, Col } from 'antd';
import './panels.scss';

interface Props extends React.HTMLProps<HTMLDivElement> {
    columns: string;
}

export default class Panels extends React.Component<Props> {
    public render(): JSX.Element | null {
        if (!this.props.children) {
            return null;
        }

        const columns: string[] = this.props.columns.split('-');
        const cols: any[][] = [];
        for (const _ of columns) {
            cols.push([]);
        }
        if (Array.isArray(this.props.children)) {
            let i: number = 0;
            for (const child of this.props.children) {
                const cs:any[]=cols[i++ % cols.length];
                cs.push(child);
                cs.push(<div key={'space-'+i} className="panel-division"/>);
            }
        } else {
            cols[0].push(this.props.children);
        }

        return (
            <Row gutter={8}>
                {columns.map((column, index) =>
                    <Col key={index} span={parseInt(column, 10)}>
                        {cols[index]}
                    </Col>
                )}
            </Row>
        );
    }
}