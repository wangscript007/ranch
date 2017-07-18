import React from "react";
import "./index.css";

class Grid extends React.Component {
    render() {
        return (
            <div className="grid">
                <table cellSpacing="1px">
                    <thead>
                        <tr>
                            <th>标题1</th>
                            <th>标题2</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>haha</td>
                            <td>hello</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        );
    }
}

export default Grid;