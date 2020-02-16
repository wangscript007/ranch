import React from 'react';
import { Menu, Icon } from 'antd';
import { service } from '../http';
import body from './body';

const { SubMenu } = Menu;

class LeftMenu extends React.Component {
    constructor() {
        super();

        this.state = {
            items: []
        };
        this.map = {};
        service('/ui/console/menu', { domain: 'console' }).then(data => this.setState({ items: data }));
    }

    click = e => {
        let item = this.map[e.key];
        body.load(item.service, item.parameter, null);
    };

    render = () => <Menu onClick={this.click} mode="inline" theme="dark">{this.menu(this.state.items)}</Menu>;

    menu = items => {
        let menus = [];
        if (items.length === 0) return menus;

        for (let item of items) {
            let key = Math.random().toString(36).substring(2);
            let title = [<Icon key="icon" type={item.icon || 'blank'} />, <span key="label">{item.label}</span>];
            if (item.items) {
                menus.push(<SubMenu key={key} title={<span>{title}</span>} >{this.menu(item.items)}</SubMenu>);
            } else {
                this.map[key] = item;
                menus.push(<Menu.Item key={key}>{title}</Menu.Item>);
            }
        }

        return menus;
    };
}

export default LeftMenu;