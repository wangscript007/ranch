import React from 'react';
import { ConfigProvider } from 'antd';
import zhCN from 'antd/es/locale/zh_CN';
import { service, loader } from '../http';
import Console from '../console';
import SignIn from '../sign-in';

class Main extends React.Component {
  constructor() {
    super();

    this.state = {
      user: {}
    };
    loader(null);
    service('/user/sign').then(data => this.setState({ user: data }));
  }

  render = () => (
    <ConfigProvider locale={zhCN}>
      {this.state.user.id && this.state.user.id.length === 36 ? <Console user={this.state.user} /> : <SignIn />}
    </ConfigProvider>
  );
}

export default Main;