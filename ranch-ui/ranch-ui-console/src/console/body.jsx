import React from 'react';
import meta from './meta';
import Grid from './grid';
import Form from './form';
import Setting from './setting';

class Body {
    setIndex = index => this.index = index;

    uri = (uri, service) => {
        if (!service) return uri;

        if (service.startsWith('/')) return service;

        return uri.substring(0, uri.lastIndexOf('/') + 1) + service;
    }

    load = (uri, parameter, data) => {
        if (!uri.startsWith('/')) uri = uri.substring(uri.indexOf('.')).replace(/\./g, '/');
        meta.get(uri).then(mt => {
            if (mt === null) return;

            let m = mt[uri.substring(uri.lastIndexOf('/') + 1)]
            if (!m) return;

            if (m.type === 'grid') {
                this.setState(<Grid columns={mt.props} meta={m} uri={uri} parameter={parameter} data={data} body={this} />);
            } else if (m.type === 'form') {
                this.setState(<Form columns={mt.props} meta={m} uri={uri} parameter={parameter} data={data} body={this} />);
            } else if (m.type === 'setting') {
                this.setState(<Setting code={mt.key} columns={mt.props} meta={m} uri={uri} parameter={parameter} data={data} body={this} />);
            }
        });
    }

    setState = state => this.index.setState({ body: <div /> }, () => this.index.setState({ body: state }));
}

const body = new Body();

export default body;