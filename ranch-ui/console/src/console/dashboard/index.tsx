import * as React from 'react';
import { Icon } from 'antd';
import Panels from './panels';
import Panel from './panel';
import datetime from '../../util/datetime';
import { pager } from '../pager';
import './index.scss';

interface State {
    count: {
        total: number;
        online: number;
    };
    status: {
        concurrent: number;
        timestamp: number;
    };
}

export default class Dashboard extends React.Component<object, State> {
    private refreshTimeout: number = 0;

    public constructor(props: object) {
        super(props);

        this.state = {
            count: {
                total: 0,
                online: 0
            },
            status: {
                concurrent: 0,
                timestamp: 0
            }
        };

        this.refresh = this.refresh.bind(this);

        this.refresh();
    }

    public render(): JSX.Element {
        return (
            <Panels columns='8-8-8'>
                <Panel title="实时数据" extra={<Icon type="sync" onClick={this.refresh} />}
                    actions={[
                        <div key="user" onClick={this.user}><Icon type="user" />用户</div>,
                        <div key="online" onClick={this.online}><Icon type="bulb" />在线</div>
                    ]}>
                    <div className="realtime-data"><span className="label">总用户数</span><span className="value">{this.state.count.total}</span></div>
                    <div className="realtime-data"><span className="label">当前在线</span><span className="value">{this.state.count.online}</span></div>
                    <div className="realtime-data"><span className="label">并发线程</span><span className="value">{this.state.status.concurrent}</span></div>
                    <div className="realtime-data"><span className="label">服务器时间</span><span className="value">{datetime.time(this.state.status.timestamp)}</span></div>
                </Panel>
            </Panels>
        );
    }

    public componentWillUnmount(): void {
        this.clearRefreshTimeout();
    }

    private refresh(): void {
        pager.post({ service: 'ranch.user.count' }).then(count => {
            if (count === null) {
                return;
            }

            pager.post({ service: '/tephra/ctrl/status' }).then(status => {
                if (status === null) {
                    return;
                }

                this.setState({
                    count: count,
                    status: status
                }, () => {
                    this.clearRefreshTimeout();
                    this.refreshTimeout = window.setTimeout(this.refresh, 10 * 1000);
                });
            });
        });
    }

    private clearRefreshTimeout(): void {
        if (this.refreshTimeout > 0) {
            window.clearTimeout(this.refreshTimeout);
        }
    }

    private user(): void {
        pager.to({
            service: 'ranch.user.query',
            parameter: {
                maxGrade: 49
            }
        });
    }

    private online(): void {
        pager.to({
            service: 'ranch.user.online.query'
        });
    }
}