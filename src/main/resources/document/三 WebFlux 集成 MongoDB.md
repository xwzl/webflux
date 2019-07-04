# 一、前言
上一讲用 Map 数据结构内存式存储了数据。这样数据就不会持久化，本文我们用 MongoDB 来实现 WebFlux 对数据源的操作。

什么是 MongoDB ?

官网：https://www.mongodb.com/

MongoDB 是一个基于分布式文件存储的数据库，由 C++ 语言编写，旨在为 WEB 应用提供可扩展的高性能数据存储解决方案。

MongoDB 是一个介于关系数据库和非关系数据库之间的产品，是非关系数据库当中功能最丰富，最像关系数据库的。

由于操作方便，本文用 Docker 启动一个 MognoDB 服务。如果 Docker 不会安装的，请参考此文：Docker 安装与基本操作 https://www.jianshu.com/p/f272726db9c5

Docker 安装 MognoDB 并启动如下：

#### 1、创建挂载目录

    docker volume create mongo_data_db
    docker volume create mongo_data_configdb
 
#### 2、启动 MognoDB

    docker run -d \
        --name mongo \
        -v mongo_data_configdb:/data/configdb \
        -v mongo_data_db:/data/db \
        -p 27017:27017 \
        mongo \
        --auth
#### 3、初始化管理员账号

    docker exec -it mongo     mongo              admin
                            // 容器名   // mongo命令 数据库名
     
    # 创建最高权限用户
    db.createUser({ user: 'admin', pwd: 'admin', roles: [ { role: "root", db: "admin" } ] });

#### 4、测试连通性

    docker run -it --rm --link mongo:mongo mongo mongo -u admin -p admin --authenticationDatabase admin mongo/admin

# 二、新增 POM 依赖与配置

在 pom.xml 配置新的依赖：

```xml
    <!-- Spring Boot 响应式 MongoDB 依赖 -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-mongodb-reactive</artifactId>
    </dependency>
```

类似配了 MySQL 和 JDBC 驱动，肯定得去配置数据库。在 application.properties 配置下上面启动的 MongoDB 配置：

数据库名为 admin、账号密码也为 admin。

```properties
spring.data.mongodb.host=localhost
spring.data.mongodb.database=admin
spring.data.mongodb.port=27017
spring.data.mongodb.username=admin
spring.data.mongodb.password=admin
```

这就一个巨大的问题了，为啥不用我们常用的 MySQL 数据库呢？

答案是 Spring Data Reactive Repositories 目前支持 Mongo、Cassandra、Redis、Couchbase。不支持 MySQL ，那究竟为啥呢？那就说明下 JDBC 和 Spring Data 的关系。

Spring Data Reactive Repositories 突出点是 Reactive，即非阻塞的。区别如下：

基于 JDBC 实现的 Spring Data ，比如 Spring Data JPA 是阻塞的。原理是基于阻塞 IO 模型
消耗每个调用数据库的线程（Connection）
事务只能在一个 java.sql.Connection 使用，即一个事务一个操作。
那如何异步非阻塞封装下 JDBC 的思想也不新鲜，Scala 库 Slick 3 就实现了。简单的实现原理如下：

一个事务多个操作，那么共享一个 java.sql.Connection 。可以使用透明事务管理，利用回调编程模型去传递
保持有限的空闲连接

最后，我坚信非阻塞 JDBC 很快就会出现的。这样我们就开心的调用 MySQL 了。

# 三、对象

```java

import org.springframework.data.annotation.Id;
 
/**
 * 城市实体类
 *
 */
public class City {
 
    /**
     * 城市编号
     */
    @Id
    private Long id;
 
    /**
     * 省份编号
     */
    private Long provinceId;
 
    /**
     * 城市名称
     */
    private String cityName;
 
    /**
     * 描述
     */
    private String description;
 
    public Long getId() {
        return id;
    }
 
    public void setId(Long id) {
        this.id = id;
    }
 
    public Long getProvinceId() {
        return provinceId;
    }
 
    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }
 
    public String getCityName() {
        return cityName;
    }
 
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
 
    public String getDescription() {
        return description;
    }
 
    public void setDescription(String description) {
        this.description = description;
    }
}
```
