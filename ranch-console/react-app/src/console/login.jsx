import React from "react";
import "./login.css";
import message from "./message.json";
import Input from "../component/input";
import Password from "../component/password";
import Button from "../component/button";

class Login extends React.Component {
    constructor(props) {
        super(props);
        this.data = {
            type: 1,
            uid: "",
            password: ""
        };
        this.signIn = this.signIn.bind(this);
    }

    render() {
        return (
            <div className="console-login">
                <Input owner={this} message={message} meta={{ label: "username", name: "uid" }} value="" />
                <Password owner={this} message={message} meta={{ label: "password", name: "password" }} value="" />
                <div className="sign-in">
                    <Button message={message} label="sign-in" click={this.signIn} />
                </div>
            </div >
        );
    }

    put(name, value) {
        this.data[name] = value;
    }

    signIn() {
        window.ajax("/console/service", this.data, { key: "ranch.user.sign-in" }).then(json => {
            if (!json || !json.hasOwnProperty("code")) {
                console.log("failure:" + JSON.stringify(json));

                return;
            }

            if (json.code !== 0) {
                // TODO 弹出提示。
                console.log(JSON.stringify(json));
                alert(json.message || json.msg);

                return;
            }

            this.props.owner.setState(prevState => ({
                sign: json.data
            }));
        });
    }
}

export default Login;