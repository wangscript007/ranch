import React from "react";
import "./index.css";
import A from "../a";
import message from "./message.json";

class Table extends React.Component {
    render() {
        var hasOp = this.props.meta.ops && this.props.meta.ops.length > 0;

        return (
            <table className="table">
                <thead>
                    <tr>
                        <th className="index"></th>
                        {this.props.meta.cols.map((col, index) => this.th(index, col))}
                        {hasOp ? <th></th> : null}
                    </tr>
                </thead>
                <tbody>
                    {!this.props.list || this.props.list.length === 0 ? this.empty(hasOp) : this.props.list.map((row, index) => this.tr(index, row, hasOp))}
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

    empty(hasOp) {
        return (
            <tr>
                <td colSpan={this.props.meta.cols.length + (hasOp ? 2 : 1)} className="empty">{window.message(message, "table.list.empty")}</td>
            </tr>
        );
    }

    tr(index, row, hasOp) {
        return (
            <tr key={index}>
                <th className="index">{index + 1}</th>
                {this.props.meta.cols.map((col, i) => this.td(row, i, col))}
                {hasOp ? this.ops(row) : null}
            </tr>
        );
    }

    td(row, index, col) {
        var value = row.hasOwnProperty(col.name) ? row[col.name] : "";
        var label = value;
        if (col.hasOwnProperty("select")) {
            for (var i = 0; i < col.select.length; i++) {
                if (col.select[i].value === value) {
                    label = window.message(this.props.meta.message, col.select[i].label);

                    break;
                }
            }
        } else if (col.type === "password")
            label = "******";

        return (
            <td key={index} className={col.type || ""}>{label}</td>
        );
    }

    ops(row) {
        return (
            <td className="ops">
                {this.props.meta.ops.map((op, index) => this.op(row, index, op))}
            </td>
        );
    }

    op(row, index, op) {
        var label = op.label || ("table.ops." + op.type);
        var data = {
            meta: this.props.meta,
            op: op,
            row: row
        };

        return (
            <A key={index} label={window.message(message, label)} click={this.opClick} data={data} />
        );
    }

    opClick(data) {
        if (data.op.service) {
            if (data.op.type === "delete")
                window.bean.get("console.body").service(data.op.service, true, { id: data.row.id }, data.op.success);

            return;
        }

        var meta = {};
        for (var key in data.meta)
            meta[key] = data.meta[key];
        for (key in data.op)
            meta[key] = data.op[key];
        window.bean.get("console.body").set(meta, data.row, false);
    }
}

export default Table;