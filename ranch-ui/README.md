# UI

[控制台](console/)

## console

```
curl -sL https://rpm.nodesource.com/setup_13.x | bash -

curl -sL https://dl.yarnpkg.com/rpm/yarn.repo | tee /etc/yum.repos.d/yarn.repo
dnf install yarn make g++

yarn create react-app ranch-ui-console
cd ranch-ui-console
yarn add antd react-app-rewired customize-cra babel-plugin-import draft-js es6-shim
sed -i 's/react-scripts /react-app-rewired /g' package.json

mkdir ranch-ui-console-pro
cd ranch-ui-console-pro
yarn create umi
yarn

yarn create react-app ranch-ui-console --template typescript
cd ranch-ui-console
yarn add antd react-app-rewired customize-cra babel-plugin-import node-sass
sed -i 's/react-scripts /react-app-rewired /g' package.json
```