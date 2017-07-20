import React from "react";
import "./index.css";
import Search from "../../component/search";
import Table from "../../component/table";
import Pagination from "../../component/pagination";

class Grid extends React.Component {
    render() {
        var hasList = this.props.data.hasOwnProperty("list");
        var list = hasList ? this.props.data.list : this.props.data;

        return (
            <div className="grid">
                {this.props.meta && this.props.meta.hasOwnProperty("search") ? <Search meta={this.props.meta.search} /> : null}
                <Table meta={this.props.meta} list={list} />
                {hasList ? <Pagination /> : null}
            </div>
        );
    }
}

export default Grid;