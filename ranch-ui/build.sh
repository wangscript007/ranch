#!/bin/bash

dir=`pwd`
cd ../ranch-web/src/main/webapp
webapp=`pwd`
ls | grep -v '^WEB-INF' | xargs rm -rf
cd $dir

name='console'
cd $name
yarn build
mv build $webapp/$name
sed -i 's,/static/,static/,g' $webapp/$name/index.html
sed -i 's,React App,Ranch UI Console,g' $webapp/$name/index.html
cd $dir
