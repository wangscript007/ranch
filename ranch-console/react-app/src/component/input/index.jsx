import React from "react";
import "./index.css";

class Input extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            value: props.value
        };

        this.change = this.change.bind(this);
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
            <input type={this.type()} name={this.props.meta.name} value={this.state.value}
                placeholder={window.message(this.props.message, this.props.meta.placeholder)}
                onChange={this.change} />
        );
    }

    type() {
        return "text";
    }

    change(event) {
        this.setState({
            value: event.target.value
        });
        if (this.props.owner)
            this.props.owner.put(this.props.meta.name, event.target.value);
    }
}

export default Input;