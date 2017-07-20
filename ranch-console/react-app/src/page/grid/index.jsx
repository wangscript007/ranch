import React from "react";
import "./index.css";
import Table from "../../component/table";
import Pagination from "../../component/pagination";

class Grid extends React.Component {
    render() {
        return (
            <div className="grid">
                <Table meta={this.props.meta} list={this.props.data.list} />
                <Pagination />
            </div>
        );
    }
}

export default Grid;