import React from "react";
import Input from "../input";
import "./index.css";

class Select extends Input {
    input() {
        return (
            <select name={this.props.meta.name}>
                {this.props.meta.select.map((option, index) => this.option(index, option))}
            </select>
        );
    }

    option(index, option) {
        return (
            <option key={index} value={option.value}>{window.message(this.props.message, option.label)}</option>
        );
    }
}

export default Select;