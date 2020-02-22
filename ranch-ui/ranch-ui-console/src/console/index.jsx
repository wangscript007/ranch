import React from 'react';
import { Layout } from 'antd';
import { url } from '../http';
import Menu from './menu';
import Sign from './sign';
import body from './body';
import './index.css';

const { Header, Footer, Sider, Content } = Layout;

class Console extends React.Component {
  constructor() {
    super();

    this.state = {
      menu: <Menu />,
      body: <div />
    };
    body.setIndex(this);
  }

  render = () => (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider>
        <div className="console-logo">{this.props.logo ? [<img key="img" src={url(this.props.logo)} alt="" />, <div key="div"></div>] : null}</div>
        {this.state.menu}
      </Sider>
      <Layout>
        <Header className="console-header">
          <Sign user={this.props.user} body={body} />
        </Header>
        <Content>
          <div className="console-body">{this.state.body}</div>
        </Content>
        <Footer className="console-footer">Copyright &copy; {new Date().getFullYear()} ranch-ui-console</Footer>
      </Layout>
    </Layout>
  );
}

export default Console;