import * as React from 'react';
import { Layout, Menu, Dropdown, Avatar, Icon } from 'antd';
import http from '../util/http';
import { service } from '../service';
import { User, user } from '../user';
import { meta } from './meta';
import { Page } from './page';
import { pager, Request } from './pager';
import './index.scss';

const { Header, Sider, Content } = Layout;
const { Divider } = Menu;
const { SubMenu } = Menu;

interface Props {
    user: User;
}

interface State {
    menus: Array<{
        icon?: string;
        label: string;
        items: MenuItem[];
    }>;
}

interface MenuItem extends Request {
    icon?: string;
    label: string;
}

export default class Console extends React.Component<Props, State> {
    public constructor(props: Props) {
        super(props);

        this.state = {
            menus: []
        };

        service.post({ uri: '/ui/console/menu' }).then(data => {
            if (data === null) {
                return;
            }

            this.setState({ menus: data.menus })
        });
    }

    public render(): JSX.Element {
        return (
            <Layout className="console">
                <Header>
                    <div className="dashboard" onClick={this.dashboard}><Icon type="dashboard" /></div>
                    <Dropdown className="user-info" overlay={
                        <Menu>
                            <Menu.Item>
                                <div onClick={this.sign}><Icon type="form" /> 修改信息</div>
                            </Menu.Item>
                            <Menu.Item>
                                <div onClick={this.password}><Icon type="lock" /> 修改密码</div>
                            </Menu.Item>
                            <Divider />
                            <Menu.Item>
                                <div onClick={user.out}><Icon type="logout" /> 登出</div>
                            </Menu.Item>
                        </Menu>
                    }>
                        <div>
                            {this.props.user.portrait ? <Avatar src={http.url(this.props.user.portrait)} /> : <Avatar icon="user" />}
                            <span className="nick">{this.props.user.nick || 'Ranch UI'}</span>
                            <Icon type="down" />
                        </div>
                    </Dropdown>
                </Header>
                <Layout>
                    <Sider>
                        <Menu mode="inline">
                            {this.state.menus.map((menu, i) =>
                                <SubMenu key={'menu' + i} title={<span><Icon type={menu.icon || 'folder'} />{menu.label}</span>}>
                                    {menu.items.map((item, j) =>
                                        <Menu.Item key={'item' + i + '-' + j} onClick={this.item.bind(this, item)}>
                                            <span><Icon type={item.icon || 'file'} />{item.label}</span>
                                        </Menu.Item>
                                    )}
                                </SubMenu>
                            )}
                        </Menu>
                    </Sider>
                    <Content>
                        <div className="content"><Page /></div>
                    </Content>
                </Layout>
            </Layout>
        );
    }

    private dashboard(): void {
        pager.dashboard();
    }

    private item(item: MenuItem): void {
        pager.to(item);
    }

    private sign(): void {
        pager.to({
            service: '/user/sign'
        });
    }

    private password(): void {
        meta.get('/user/password').then(data => {
            if (data === null) {
                return;
            }

            pager.setPage({
                service: '/user/password',
                data: {}
            });
        });
    }
}