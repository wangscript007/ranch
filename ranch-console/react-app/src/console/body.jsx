import React from "react";
import "./body.css";
import Page from "../page";

class Body extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            service: "",
            data: {}
        };
        window.bean.put("console.body", this);
    }

    service(key, loading, params, success) {
        this.loading(loading);

        var headers = { key: key };
        window.ajax("/console/service", params, headers).then(json => {
            if (!json || !json.hasOwnProperty("code")) {
                console.log("failure:" + JSON.stringify(json));
                this.loading(false);

                return;
            }

            if (json.code !== 0) {
                // TODO 弹出提示。
                console.log(JSON.stringify(json));
                alert(json.message || json.msg);
                this.loading(false);

                return;
            }

            window.meta.load(key).then(meta => this.set(key, json.data, success));
        });
    }

    loading(active) {
        window.bean.get("loading").setState(prevState => ({
            active: active
        }));
    }

    set(service, data, success) {
        if (success) {
            this.service(success, true);

            return;
        }

        this.setState(prevState => ({
            service: service,
            data: data
        }));
        this.loading(false);
    }

    render() {
        return (
            <div className="console-body">
                <Page service={this.state.service} data={this.state.data} />
            </div>
        );
    }
}

export default Body;