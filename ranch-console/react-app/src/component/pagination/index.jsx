import React from "react";
import "./index.css";
import message from "./message.json";

class Pagination extends React.Component {
    render() {
        return (
            <div className="pagination">
                {this.page(2, "<", "pagination.prev")}
                {this.page(1, 1)}
                {this.page(2, 2)}
                <span>3</span>
                {this.page(4, 4)}
                {this.page(5, 5)}
                {this.page(4, ">", "pagination.next")}
            </div>
        );
    }

    page(num, label, title) {
        return (
            // eslint-disable-next-line
            <a href="javascript:void(0);" title={window.message(message, title)}>{label}</a>
        );
    }
}

export default Pagination;