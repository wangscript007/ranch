import React from "react";
import "./index.css";
import A from "../a";
import message from "./message.json";

class Pagination extends React.Component {
    render() {
        let prev = [];
        for (let i = this.props.data.pageStart; i < this.props.data.number; i++)
            prev.push(this.page(i, i));
        let next = [];
        for (let i = this.props.data.number + 1; i <= this.props.data.pageEnd; i++)
            next.push(this.page(i, i));

        return (
            <div className="pagination">
                {this.prev()}
                {prev}
                <span>{this.props.data.number}</span>
                {next}
                {this.next()}
            </div>
        );
    }

    prev() {
        if (this.props.data.number <= 1)
            return null;

        return this.page(this.props.data.number - 1, "<", "pagination.prev");
    }

    next() {
        if (this.props.data.number >= this.props.data.page)
            return null;

        return this.page(this.props.data.number + 1, ">", "pagination.next");
    }

    page(num, label, title) {
        let data = {
            service: this.props.service,
            num: num
        };

        return (
            <A key={num} message={message} title={title} label={label} data={data} click={this.click} />
        );
    }

    click(data) {
        window.bean.get("console.body").service(data.service, true, { pageNum: data.num });
    }
}

export default Pagination;