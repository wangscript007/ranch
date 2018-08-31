import * as React from 'react';
import { Layout, Menu, Dropdown, Avatar, Icon } from 'antd';
import storage from '../util/storage';
import http from '../util/http';
import { service, User } from '../service';
import { Page } from './page';
import { pager } from './pager';
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

interface MenuItem {
    icon?: string;
    label: string;
    service: string;
    header?: {};
    parameter?: {};
}

export default class Console extends React.Component<Props, State> {
    public constructor(props: Props) {
        super(props);

        this.state = {
            menus: []
        };

        service.post('/ui/console/menu').then(data => {
            if (data === null) {
                return;
            }

            this.setState({ menus: data })
        });
    }

    public render(): JSX.Element {
        return (
            <Layout className="console">
                <Header>
                    <Dropdown className="user-info" overlay={
                        <Menu>
                            <Menu.Item>
                                <div onClick={this.sign}><Icon type="form" /> 修改信息</div>
                            </Menu.Item>
                            <Divider />
                            <Menu.Item>
                                <div onClick={this.signOut}><Icon type="logout" /> 登出</div>
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

    private item(item: MenuItem): void {
        pager.to(item.service, item.header, item.parameter);
    }

    private sign(): void {
        pager.to('ranch.user.sign');
    }

    private signOut(): void {
        service.post('/user/sign-out').then(data => {
            storage.remove('tephra-session-id');
            location.reload();
        });
    }
}