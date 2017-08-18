import React from "react";
import "./index.css";
import Search from "../../component/search";
import Table from "../../component/table";
import Toolbar from "../../component/toolbar";
import Pagination from "../../component/pagination";

class Grid extends React.Component {
    render() {
        var hasList = this.props.data.hasOwnProperty("list");
        var list = hasList ? this.props.data.list : this.props.data;

        return (
            <div className="grid">
                <Search meta={this.props.meta} />
                <Table meta={this.props.meta} list={list} />
                <Toolbar meta={this.props.meta} />
                {hasList ? <Pagination /> : null}
            </div>
        );
    }
}

export default Grid;