spring:
  jackson:
    default-property-inclusion: non_null
  datasource:
    driver-class-name: ${driver_class}
    url: ${url}
    username: ${username}
    password: ${password}

# mybatis配置
mybatis:
  # 配置映射类所在包名
  # type-aliases-package: ${type_aliases_package}
  # 配置mapper xml文件所在路径，这里是一个数组
  mapper-locations:
    - mappers/**/*.xml

mapper:
    mappers:
        - ${coreMapperPath}
    not-empty: false
    identity: MYSQL

pagehelper:
    helperDialect: mysql
    reasonable: true
    supportMethodsArguments: true
    params: count=countSql