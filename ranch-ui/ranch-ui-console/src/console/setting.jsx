import { service } from '../http';
import Form from './form';

class Setting extends Form {
    load = () => service('/classify/list', { code: this.props.code }).then(data => {
        if (data === null) return null;

        let kvs = {};
        for (let column of data) {
            kvs[column.key] = column.value;
        }

        return kvs;
    });
}

export default Setting;