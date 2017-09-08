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
        var value = window.json.get(row, col.name);
        var label = value;
        if (col.hasOwnProperty("select"))
            label = this.select(value, col.select);
        else if (col.type === "password")
            label = "******";
        if (typeof (label) === "object")
            label = JSON.stringify(label);

        return (
            <td key={index} className={col.format || ""}>{label}</td>
        );
    }

    select(value, select) {
        if (select.hasOwnProperty("label")) {
            if (!select.array)
                select.array = select.label.split(",");

            return select.array[value];
        }

        return value;
    }

    ops(row) {
        return (
            <td className="ops">
                {this.meta.ops.map((op, index) => this.op(index, op, row))}
            </td>
        );
    }

    op(index, op, row) {
        if (op.hasOwnProperty("when"))
            for (var key in op.when)
                if (op.when[key] !== row[key])
                    return null;

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