import React from "react";
import "./index.css";
import Search from "../../component/search";
import Table from "../../component/table";
import Toolbar from "../../component/toolbar";
import Pagination from "../../component/pagination";

class Grid extends React.Component {
    render() {
        return (
            <div className="grid">
                <Search service={this.props.service} />
                <Table service={this.props.service} list={this.props.data.list || this.props.data} />
                {this.props.data.hasOwnProperty("list") ? <Pagination service={this.props.service} data={this.props.data} /> : null}
                <Toolbar service={this.props.service} />
            </div>
        );
    }
}

export default Grid;