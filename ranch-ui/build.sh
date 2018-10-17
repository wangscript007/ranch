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
cd $dir
