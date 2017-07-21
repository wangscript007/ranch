import React from "react";
import "./index.css";
import message from "./message.json";

class Table extends React.Component {
    render() {
        return (
            <table className="table">
                <thead>
                    <tr>
                        <th className="index"></th>
                        {this.props.meta.cols.map((col, index) => this.th(index, col))}
                    </tr>
                </thead>
                <tbody>
                    {!this.props.list || this.props.list.length === 0 ? this.empty() : this.props.list.map((row, index) => this.tr(index, row))}
                </tbody>
            </table>
        );
    }

    th(index, col) {
        return (
            <th key={index}>
                {window.message(this.props.meta.message, col.label)}
            </th>
        );
    }

    empty() {
        return (
            <tr>
                <td colSpan={this.props.meta.cols.length + 1} className="empty">{window.message(message, "table.list.empty")}</td>
            </tr>
        );
    }

    tr(index, row) {
        return (
            <tr key={index}>
                <th className="index">{index + 1}</th>
                {this.props.meta.cols.map((col, i) => this.td(row, i, col))}
            </tr>
        );
    }

    td(row, index, col) {
        var value = row.hasOwnProperty(col.name) ? row[col.name] : "";
        if (col.hasOwnProperty("select"))
            value = window.message(this.props.meta.message, col.select[value]);

        return (
            <td key={index} className={col.type || ""}>{value}</td>
        );
    }
}

export default Table;