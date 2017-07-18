import React from "react";
import message from "./message.json";
import "./top.css";
import Menu from "./menu";

class Top extends React.Component {
    render() {
        return (
            <div className="console-top">
                <span className="project-name">{window.message(message, "project-name")}</span>
                {this.link(window.message(message, "sign-out"))}
                <Menu service="dashboard">{window.message(message, "dashboard")}</Menu>
            </div>
        );
    }

    link(label) {
        return (
            // eslint-disable-next-line
            <a href="javascript:void(0);">{label}</a>
        );
    }
}

export default Top;
