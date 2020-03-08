import React from 'react';
import { Select, Tree, Icon, Button } from 'antd';
import { service } from '../http';
import './crosier.css';

class Crosier extends React.Component {
    constructor(props) {
        super(props);

        this.state = {};
        service('/user/crosier/grades').then(data => {
            if (data == null) return;

            this.setState({
                grades: data,
                grade: 0
            }, () => this.grade(0));
        });
        service('/ui/console/menu', { domain: "console", all: true }).then(data => {
            if (data === null) return;

            this.setState({ menu: data });
        });
    }

    grade = grade => {
        service('/user/crosier/pathes', { grade: grade }).then(data => {
            if (data === null) return;

            this.setState({
                grade: grade,
                pathes: data
            });
        });
    }

    check = selects => {
        this.setState({
            selects: selects,
            pathes: selects
        });
    }

    save = () => {
        if (this.state.selects === undefined) return;

        service('/user/crosier/save', { grade: this.state.grade, pathes: this.state.selects.join(',,') });
    }

    render = () => {
        let elements = [];
        if (!this.state.grades || this.state.grades.length === 0) return elements;

        elements.push(this.grades());
        if (this.state.menu && this.state.menu.length > 0)
            elements.push(<Tree key="menu" checkable={true} selectable={false} showIcon={true} checkedKeys={this.state.pathes} onCheck={this.check}>{this.nodes(this.state.menu, '')}</Tree>);
        elements.push(<div key="toolbar" className="console-crosier-toolbar"><Button type="primary" onClick={this.save}>保存</Button></div>);

        return elements;
    }

    grades = () => {
        let options = [];
        for (let grade of this.state.grades) {
            options.push(<Select.Option key={grade.grade} value={grade.grade}>{grade.name}</Select.Option>);
        }

        return <Select key="grades" defaultValue={this.state.grade || 0} style={{ width: '100%' }} onChange={this.grade}>{options}</Select>;
    }

    nodes = (menus, parentKey) => {
        let nodes = [];
        if (!menus || menus.length === 0) return nodes;

        let keys = {};
        for (let menu of menus) {
            let key = parentKey + (menu.service || menu.type || menu.label);
            if (menu.parameter)
                key += JSON.stringify(menu.parameter);
            if (key in keys) continue;

            keys[key] = true;
            nodes.push(<Tree.TreeNode icon={<Icon type={menu.icon || 'blank'} />} title={menu.label} key={key}>{this.nodes(menu.items, key + ';')}</Tree.TreeNode>);
        }

        return nodes;
    }
}

export default Crosier;