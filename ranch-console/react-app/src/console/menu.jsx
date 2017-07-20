import React from "react";
import "./menu.css";

class Menu extends React.Component {
    constructor(props) {
        super(props);

        this.click = this.click.bind(this);
    }

    click() {
        window.bean.get("console.body").service(this.props.service, true);
    }

    render() {
        return (
            // eslint-disable-next-line
            <a className="menu" href="javascript:void(0);" onClick={this.click}>{this.props.children}</a>
        );
    }
}

export default Menu;