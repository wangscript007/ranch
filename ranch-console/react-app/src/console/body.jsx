import React from "react";
import "./body.css";

class Body extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: "Hello body"
        };
        window.service.register("console.body", this);
    }

    render() {
        return (
            <div className="console-body">
                {this.state.loading ? "loading" : JSON.stringify(this.state.data)}
            </div>
        );
    }
}

export default Body;