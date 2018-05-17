#!/bin/sh
# author:gaowenfeng
# springboot项目生成
echo 正在生成.....
spring init \
    --build=maven \
    --java-version=1.8 \
    --dependencies=web,mysql,mybatis,lombok \
    --packaging=jar \
    --groupId=$1 \
    --artifactId=$2 \
    --description=$4 \
    --version=$5 \
    $3
echo 生成成功.....
