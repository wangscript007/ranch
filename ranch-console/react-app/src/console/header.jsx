import React from "react";
import message from "./message.json";
import "./header.css";
import Menu from "./menu";
import A from "../component/a";

class Header extends React.Component {
    constructor(props) {
        super(props);

        this.signOut = this.signOut.bind(this);
    }

    render() {
        return (
            <div className="console-header">
                <span className="project-name">{window.message(message, "project-name")}</span>
                {this.link(window.message(message, "sign-out"))}
                <Menu service="dashboard">{window.message(message, "dashboard")}</Menu>
            </div>
        );
    }

    link(label) {
        return (
            <A message={message} label="sign-out" click={this.signOut} />
        );
    }

    signOut() {
        window.ajax("/console/service", {}, { key: "ranch.user.sign-out" }).then(json => {
            this.props.owner.setState(prevState => ({
                sign: {}
            }));
        });
    }
}

export default Header;
