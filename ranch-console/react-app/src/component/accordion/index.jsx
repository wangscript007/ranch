import React from "react";
import "./index.css";

class Accordion extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            active: props.active
        };
        this.click = this.click.bind(this);
    }

    click() {
        this.setState(prevState => ({
            active: !prevState.active
        }));
    }

    render() {
        return (
            <div className={"accordion accordion-" + (this.state.active ? "" : "in") + "active"}>
                {this.title(this.props.title)}
                <div className="accordion-body">
                    {this.props.children}
                </div>
            </div>
        );
    }

    title(label) {
        return (
            // eslint-disable-next-line
            <a className="accordion-title" href="javascript:void(0);" onClick={this.click}>{label}</a>
        );
    }
}

export default Accordion;