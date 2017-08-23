import React from "react";
import "./index.css";
import pages from "./pages";

class Page extends React.Component {
    render() {
        var tag = {
            name: pages[window.meta.page(this.props.service) || "dashboard"]
        };

        return (
            <div className="page"><tag.name service={this.props.service} data={this.props.data} /></div>
        );
    }
}

export default Page;