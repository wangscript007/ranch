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
        var search = window.meta.search(this.props.service);
        if (!search || !search.length)
            return null;

        return (
            <div className="search">
                {search.map((col, index) => this.input(index, col))}
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