import {
    message
} from 'antd';

const root = 'http://192.168.0.106:8080';

const service = (uri, body) => post(uri, body).then(json => {
    if (json === null) return null;

    if (json.code === 0) return json.data;

    message.warn('[' + json.code + ']' + json.message);
    if (json.code === 115901) {
        window.location.reload();
    }

    return null;
});

const post = (uri, body) => fetch(root + uri, {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json',
        'tephra-session-id': tsid()
    },
    body: JSON.stringify(body)
}).then(response => {
    if (post.loader) {
        post.loader.setState({
            loading: false
        });
    }

    if (response.ok) return response.json();

    message.warn('[' + response.status + ']' + response.statusText);

    return null;
});

const url = uri => root + uri;

const tsid = () => {
    if (post.loader) {
        post.loader.setState({
            loading: true
        });
    }

    let tsid = localStorage.getItem('tephra-session-id');
    if (!tsid) {
        tsid = '';
        while (tsid.length < 64) tsid += Math.random().toString(36).substring(2);
        tsid = tsid.substring(0, 64);
        localStorage.setItem('tephra-session-id', tsid);
    }

    return tsid;
}

const loader = loader => post.loader = loader;

export {
    service,
    post,
    url,
    loader
};