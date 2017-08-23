import React from "react";

class Hidden extends React.Component {
    render() {
        return (
            <input type="hidden" name={this.props.name} value={this.props.value} />
        );
    }
}

export default Hidden;