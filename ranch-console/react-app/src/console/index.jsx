import React from "react";
import message from "./message.json";
import Top from "./top";
import Left from "./left";
import Footer from "./footer";
import Body from "./body";
import Loading from "./loading";

class Console extends React.Component {
    constructor(props) {
        super(props);

        window.document.title = window.message(message, "project-name");
    }

    render() {
        return (
            <div className="console">
                <Top />
                <Left />
                <Footer />
                <Body />
                <Loading />
            </div>
        );
    }
}

export default Console;
