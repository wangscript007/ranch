import React from "react";
import "./index.css";
import Table from "../../component/table";

class Grid extends React.Component {
    render() {
        return (
            <div className="grid">
                <Table meta={this.props.meta} list={this.props.data.list} />
            </div>
        );
    }
}

export default Grid;