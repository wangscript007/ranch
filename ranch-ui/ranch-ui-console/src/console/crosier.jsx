import React from 'react';
import { Select } from 'antd';
import { service } from '../http';
import './crosier.css';

class Crosier extends React.Component {
    constructor(props) {
        super(props);

        this.state = {};
        service('/user/crosier/grades').then(data => {
            if (data == null) return;

            this.setState({ grades: data });
        }).then(()=>{
            console.log(1111);
        });
    }

    grade = grade => {
        console.log(typeof grade);
        console.log(grade);
    }

    render = () => {
        let elements = [];
        if (!this.state.grades || this.state.grades.length === 0) return elements;

        elements.push(this.grades());

        return elements;
    }

    grades = () => {
        let options = [];
        for (let grade of this.state.grades) {
            options.push(<Select.Option key={grade.grade} value={grade.grade}>{grade.name}</Select.Option>);
        }

        return <Select key="grades" defaultValue={this.state.grade || 0} style={{ width: '100%' }} onChange={this.grade}>{options}</Select>
    }
}

export default Crosier;