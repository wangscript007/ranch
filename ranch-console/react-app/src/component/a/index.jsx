import React from "react";
import "./index.css";

class A extends React.Component {
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
            // eslint-disable-next-line
            <a href={this.props.href || "javascript:void(0);"} title={window.message(this.props.message, this.props.title)} onClick={this.click}>
                {window.message(this.props.message, this.props.children || this.props.label)}
            </a>
        );
    }
}

export default A;