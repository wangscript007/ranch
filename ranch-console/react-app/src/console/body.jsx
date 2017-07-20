import React from "react";
import "./body.css";
import Page from "../page";

class Body extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            meta: { page: "dashboard" }
        };
        this.meta = {};
        window.bean.put("console.body", this);
    }

    service(key, loading, args) {
        window.bean.get("loading").setState(prevState => ({
            active: loading
        }));

        if (!args)
            args = {};
        args["service"] = key;

        window.ajax("/console/service", args).then(json => {
            if (!json || !json.hasOwnProperty("code")) {
                console.log("failure:" + JSON.stringify(json));

                return;
            }

            if (json.code !== 0) {
                // TODO 弹出提示。
                console.log(JSON.stringify(json));

                return;
            }

            if (window.meta.get(key)) {
                this.set(window.meta.get(key), json.data);
            } else {
                window.ajax("/console/meta", args).then(meta => {
                    window.meta.put(key, meta.data);
                    this.set(meta.data, json.data);
                });
            }
        });
    }

    set(meta, data) {
        this.setState(prevState => ({
            meta: meta,
            data: data
        }));

        window.bean.get("loading").setState(prevState => ({
            active: false
        }));
    }

    render() {
        return (
            <div className="console-body">
                <Page meta={this.state.meta} data={this.state.data} />
            </div>
        );
    }
}

export default Body;