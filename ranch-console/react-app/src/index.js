import React from "react";
import ReactDOM from "react-dom";
import "./index.css";
import "./util";
import Console from "./console";
import "./module";
import registerServiceWorker from "./registerServiceWorker";

ReactDOM.render(<Console />, document.getElementById("root"));
registerServiceWorker();
