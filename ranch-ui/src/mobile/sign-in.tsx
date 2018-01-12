import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from '../registerServiceWorker';
import './sign-in.less';

class SignIn extends React.Component {
    render(): JSX.Element {
        return (
            <div id="ranch-ui-sign-in">
                <div className="label">手机号</div>
                <div><input type="text" name="uid" placeholder="请输入您的手机号" /></div>
                <div className="label">密码</div>
                <div><input type="password" name="password" placeholder="请输入您的密码" /></div>
            </div>
        );
    }
}

ReactDOM.render(
    <SignIn />,
    document.getElementById('root') as HTMLElement
);
registerServiceWorker();
