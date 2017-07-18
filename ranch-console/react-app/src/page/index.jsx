import React from "react";
import "./index.css";
import pages from "./pages";

class Page extends React.Component {
    render() {
        const tag = {
            name: pages[this.props.data.page]
        };

        return (
            <div className="page"><tag.name data={this.props.data} /></div>
        );
    }
}

export default Page;