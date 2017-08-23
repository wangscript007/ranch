import React from "react";
import "./index.css";
import A from "../a";
import message from "./message.json";

class Table extends React.Component {
    render() {
        this.meta = {
            props: window.meta.props(this.props.service),
            ops: window.meta.ops(this.props.service)
        };
        this.meta.hasOp = this.meta.ops && this.meta.ops.length > 0;
        var hasList = !this.props.list || this.props.list.length === 0;

        return (
            <table className="table">
                <thead>
                    <tr>
                        <th className="index"></th>
                        {this.meta.props.map((col, index) => this.th(index, col))}
                        {this.meta.hasOp ? <th></th> : null}
                    </tr>
                </thead>
                <tbody>
                    {hasList ? this.empty() : this.props.list.map((row, index) => this.tr(index, row))}
                </tbody>
            </table>
        );
    }

    th(index, col) {
        return (
            <th key={index}>
                {window.message(this.props.message, col.label)}
            </th>
        );
    }

    empty() {
        return (
            <tr>
                <td colSpan={this.meta.props.length + (this.meta.hasOp ? 2 : 1)} className="empty">{window.message(message, "table.list.empty")}</td>
            </tr>
        );
    }

    tr(index, row) {
        return (
            <tr key={index}>
                <th className="index">{index + 1}</th>
                {this.meta.props.map((col, i) => this.td(row, i, col))}
                {this.meta.hasOp ? this.ops(row) : null}
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
            <td key={index} className={col.format || ""}>{label}</td>
        );
    }

    ops(row) {
        return (
            <td className="ops">
                {this.meta.ops.map((op, index) => this.op(index, op, row))}
            </td>
        );
    }

    op(index, op, row) {
        var label = op.label || ("table.ops." + op.type);
        var data = {
            service: this.props.service,
            op: op,
            row: row
        };

        return (
            <A key={index} label={window.message(message, label)} click={this.opClick} data={data} />
        );
    }

    opClick(data) {
        if (data.op.service) {
            window.bean.get("console.body").service(data.op.service, true, data.row, data.op.success);

            return;
        }

        var service = window.meta.prefix(data.service, 1) + data.op.type;
        window.bean.get("console.body").set(service, data.row, false);
    }
}

export default Table;