import React from "react";
import "./index.css";
import Input from "../input";
import Select from "../select";
import message from "./message.json";

const inputs = {
    "input": Input,
    "select": Select
}

class Search extends React.Component {
    render() {
        if (!this.props.meta)
            return null;

        var cols = [];
        this.props.meta.cols.map(col => {
            if (col.hasOwnProperty("search"))
                cols[cols.length] = col;

            return null;
        });
        if (cols.length === 0)
            return null;

        return (
            <div className="search">
                {cols.map((col, index) => this.input(index, col))}
                <button>{window.message(message, "button.search")}</button>
            </div>
        );
    }

    input(index, col) {
        if (!col.hasOwnProperty("search"))
            return null;

        var tag = {
            name: inputs.hasOwnProperty(col.search) ? inputs[col.search] : Input
        };

        return (
            <tag.name key={index} message={this.props.meta.message} meta={col} inline={true} />
        );
    }
}

export default Search;