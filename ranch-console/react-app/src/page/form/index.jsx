import React from "react";
import "./index.css";
import Hidden from "../../component/hidden";
import Input from "../../component/input";

class Form extends React.Component {
    render() {
        return (
            <div className="form">
                <Hidden name="id" value={this.props.data.id || ""} />
                {this.props.meta.cols.map((col, index) => this.col(index, col))}
            </div>
        );
    }

    col(index, col) {
        return (
            <Input key={index} meta={col} value={this.props.data[col.name] || ""} />
        );
    }
}

export default Form;