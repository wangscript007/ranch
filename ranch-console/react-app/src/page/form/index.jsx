import React from "react";
import "./index.css";
import Hidden from "../../component/hidden";
import Input from "../../component/input";
import Readonly from "../../component/readonly";
import Password from "../../component/password";
import Textarea from "../../component/textarea";
import Toolbar from "../../component/toolbar";

const tags = {
    input: Input,
    readonly: Readonly,
    password: Password,
    textarea: Textarea
};

class Form extends React.Component {
    constructor(props) {
        super(props);

        this.data = {
            id: this.props.data.id
        };
    }

    render() {
        this.meta = {
            props: window.meta.props(this.props.service),
            ops: window.meta.ops(this.props.service)
        };

        return (
            <div className="form">
                <Hidden owner={this} name="id" value={this.props.data.id || ""} />
                {this.meta.props.map((prop, index) => this.prop(index, prop))}
                <Toolbar owner={this} service={this.props.service} />
            </div>
        );
    }

    prop(index, prop) {
        var value = this.props.data[prop.name] || "";
        if (this.props.data[prop.name] === 0)
            value = 0;
        this.put(prop.name, value);
        var tag = {
            name: tags[prop.type || "input"] || Input
        }

        return (
            <tag.name owner={this} key={index} meta={prop} value={value} />
        );
    }

    put(name, value) {
        this.data[name] = value;
    }

    get() {
        return this.data;
    }
}

export default Form;