import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import "./util/message";
import "./util/http";
import "./util/service";
import Console from "./console";
import registerServiceWorker from "./registerServiceWorker";

ReactDOM.render(<Console />, document.getElementById("root"));
registerServiceWorker();
