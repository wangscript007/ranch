import React from "react";
import message from "./message.json";
import Login from "./login";
import Header from "./header";
import Left from "./left";
import Bottom from "./bottom";
import Body from "./body";
import Loading from "./loading";

class Console extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            sign: {}
        };

        window.document.title = window.message(message, "project-name");

        window.ajax("/console/service", {}, { key: "ranch.user.sign" }).then(json => {
            this.setState(prevState => ({
                sign: json.data
            }));
        });
    }

    render() {
        if (!this.state.sign.id) {
            return (
                <Login owner={this} />
            );
        }


        return (
            <div className="console">
                <Header owner={this} />
                <Left />
                <Bottom />
                <Body owner={this} />
                <Loading />
            </div>
        );
    }
}

export default Console;
