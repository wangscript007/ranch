import React from "react";
import "./index.css";

class Button extends React.Component {
    constructor(props) {
        super(props);

        this.click = this.click.bind(this);
    }

    click() {
        if (this.props.click)
            this.props.click(this.props.data);
    }

    render() {
        return (
            <button onClick={this.click}>
                {window.message(this.props.message, this.props.children || this.props.label)}
            </button>
        );
    }
}

export default Button;