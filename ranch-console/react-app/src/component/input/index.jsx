import React from "react";
import "./index.css";

class Input extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            value: props.value
        };
    }

    render() {
        return (
            <div className={"input-" + (this.props.inline ? "in" : "new-") + "line"}>
                {this.props.meta.label ? <label>{window.message(this.props.message, this.props.meta.label)}</label> : null}
                {this.input()}
            </div>
        );
    }

    input() {
        return (
            <input type={this.type()} name={this.props.meta.name} defaultValue={this.state.value}
                placeholder={window.message(this.props.message, this.props.meta.placeholder)} />
        );
    }

    type() {
        return "text";
    }
}

export default Input;