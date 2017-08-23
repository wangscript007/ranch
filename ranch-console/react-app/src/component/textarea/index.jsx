import React from "react";
import Input from "../input";

class Textarea extends Input {
    input() {
        return (
            <textarea name={this.props.meta.name} value={this.state.value}
                placeholder={window.message(this.props.message, this.props.meta.placeholder)}
                onChange={this.change} />
        );
    }
}

export default Textarea;