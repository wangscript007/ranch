import React from "react";
import "./loading.css";

class Loading extends React.Component {
    constructor(props) {
        super(props);
        this.state = { active: false };

        window.bean.put("loading", this);
    }

    render() {
        return (
            <div className={"loading-" + (this.state.active ? "" : "in") + "active"}>loading....</div>
        );
    }
}

export default Loading;