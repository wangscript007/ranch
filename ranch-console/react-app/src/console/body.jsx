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

    service(key, loading, params) {
        this.loading(loading);

        var headers = { service: key };
        window.ajax("/console/service", params, headers).then(json => {
            if (!json || !json.hasOwnProperty("code")) {
                console.log("failure:" + JSON.stringify(json));
                this.loading(false);

                return;
            }

            if (json.code !== 0) {
                // TODO 弹出提示。
                console.log(JSON.stringify(json));
                alert(json.message);
                this.loading(false);

                return;
            }

            if (window.meta.get(key)) {
                this.set(window.meta.get(key), json.data);
            } else {
                window.ajax("/console/meta", params, headers).then(meta => {
                    window.meta.put(key, meta.data);
                    this.set(meta.data, json.data);
                });
            }
        });
    }

    loading(active) {
        window.bean.get("loading").setState(prevState => ({
            active: active
        }));
    }

    set(meta, data) {
        this.setState(prevState => ({
            meta: meta,
            data: data
        }));
        this.loading(false);
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