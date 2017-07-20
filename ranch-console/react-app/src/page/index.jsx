import React from "react";
import "./index.css";
import pages from "./pages";

class Page extends React.Component {
    render() {
        const tag = {
            name: pages[this.props.meta.page ? this.props.meta.page : "dashboard"]
        };

        return (
            <div className="page"><tag.name meta={this.props.meta} data={this.props.data} /></div>
        );
    }
}

export default Page;