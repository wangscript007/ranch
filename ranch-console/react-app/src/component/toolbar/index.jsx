import React from "react";
import "./index.css";
import Button from "../button";
import message from "./message.json";

class Toolbar extends React.Component {
    render() {
        var toolbar = window.meta.toolbar(this.props.service);
        if (!toolbar || toolbar.length === 0)
            return null;

        return (
            <div className="toolbar">{toolbar.map((btn, index) => this.btn(index, btn))}</div>
        );
    }

    btn(index, btn) {
        var label = window.message(message, btn.label || ("toolbar." + btn.type));
        var data = {
            service: this.props.service,
            btn: btn,
            owner: this.props.owner
        };

        return (
            <Button key={index} click={this.click} data={data}>{label}</Button>
        );
    }

    click(data) {
        var param = data.btn.data === "true" ? (data.owner.get() || {}) : {};
        console.log(param);

        if (data.btn.service) {
            window.bean.get("console.body").service(data.btn.service, true, param, data.btn.success);

            return;
        }

        var service = window.meta.prefix(data.service, 1) + data.btn.type;
        window.bean.get("console.body").set(service, param, false);
    }
}

export default Toolbar;