import React from 'react';
import { service } from '../http';
import Console from '../console';
import SignIn from '../sign-in';

class Main extends React.Component {
  constructor() {
    super();

    service('/user/sign').then(data => this.setState({ user: data }));
  }

  render = () => this.state && this.state.user && this.state.user.id && this.state.user.id.length === 36 ? <Console user={this.state.user} /> : <SignIn />;
}

export default Main;