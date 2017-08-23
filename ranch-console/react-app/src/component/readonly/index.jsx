import React from "react";
import Input from "../input";
import "./index.css";

class Readonly extends Input {
    input() {
        return (
            <div className="readonly">{this.props.value}</div>
        );
    }
}

export default Readonly;