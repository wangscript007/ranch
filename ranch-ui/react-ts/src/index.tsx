import * as React from 'react';
import * as ReactDOM from 'react-dom';
import registerServiceWorker from './registerServiceWorker';
import './index.less';

ReactDOM.render(
  <div>Hello React Typescript.</div>,
  document.getElementById('root') as HTMLElement
);
registerServiceWorker();
