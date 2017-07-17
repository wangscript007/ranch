import React from "react";
import "./left.css";
import Accordion from "../component/accordion";
import Menu from "./menu";

class Left extends React.Component {
    constructor(props) {
        super(props);

        this.state = { data: [] };

        window.ajax("/console/menu").then(json => {
            this.setState(prevState => ({ data: json.data }));
        });
    }

    render() {
        return (
            <div className="console-left">
                {
                    this.state.data.map(menu =>
                        <Accordion key={menu.name} title={menu.name}>
                            {
                                menu.items.map(item =>
                                    <Menu key={item.service} service={item.service}>{item.name}</Menu>
                                )
                            }
                        </Accordion>
                    )
                }
            </div>
        );
    }
}

export default Left;