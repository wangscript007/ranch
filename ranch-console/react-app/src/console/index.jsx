import React from "react";
import message from "./message.json";
import Top from "./top";
import Left from "./left";
import Footer from "./footer";
import Body from "./body";

class Console extends React.Component {
    render() {
        window.document.title = window.message(message, "project-name");

        return (
            <div>
                <Top />
                <Left  console={this}/>
                <Footer />
                <Body console={this}/>
            </div>
        );
    }
}

export default Console;
