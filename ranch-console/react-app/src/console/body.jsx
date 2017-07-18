import React from "react";
import "./body.css";
import Page from "../page";

class Body extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            data: { page: "dashboard" }
        };
        window.service.register("console.body", this);
    }

    render() {
        return (
            <div className="console-body">
                <div className="body-area">{this.state.loading ? this.loading() : this.page()}</div>
            </div>
        );
    }

    loading() {
        return (
            <div className="loading">loading...</div>
        );
    }

    page() {
        return (
            <Page data={this.state.data} />
        );
    }
}

export default Body;