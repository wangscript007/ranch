# 控制台

TypeScript + React + ant.design

```
curl -sL https://rpm.nodesource.com/setup_13.x | bash -
dnf install -y nodejs
npm install -g yarn@berry
npm install -g typescript

yarn create react-app console --template typescript

yarn start
```

> 可能需要修改[http](src/util/http.ts)设置API访问。