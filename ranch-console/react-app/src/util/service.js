class Service {
    constructor() {
        this.map = {};
    }

    register(key, object) {
        this.map[key] = object;
    }

    data(key, data) {
        var object = this.map[key];
        if (!object)
            return;

        object.setState(prevState => ({
            data: data
        }));
    }

    execute(key, args, loading) {
        var object = this.map[key];
        if (!object)
            return;

        object.setState(prevState => ({
            data: loading
        }));

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

            object.setState(prevState => ({
                loading: false,
                data: json.data
            }));
        });
    }
}

window.service = new Service();