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
                        {this.props.meta.headers.map((th, index) => this.th(index, th))}
                    </tr>
                </thead>
                <tbody>
                    {!this.props.list || this.props.list.length === 0 ? this.empty() : this.props.list.map((tr, index) => this.tr(index, tr))}
                </tbody>
            </table>
        );
    }

    th(index, th) {
        return (
            <th key={index}>
                {window.message(this.props.meta.message, th.label)}
            </th>
        );
    }

    empty() {
        return (
            <tr>
                <td colSpan={this.props.meta.headers.length + 1} className="empty">{window.message(message, "table.list.empty")}</td>
            </tr>
        );
    }

    tr(index, tr) {
        return (
            <tr key={index}>
                <th className="index">{index + 1}</th>
                {this.props.meta.names.map((td, i) => this.td(tr, i, td))}
            </tr>
        );
    }

    td(tr, index, td) {
        return (
            <td key={index}>{tr.hasOwnProperty(td) ? tr[td] : ""}</td>
        );
    }
}

export default Table;