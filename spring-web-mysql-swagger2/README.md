
### 记录
1. 依赖只选择web下的web即可；
依赖会多一个web的依赖：
```
         <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```

2. mysql 集成
依赖添加：
```
 <!-- 数据库依赖 -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
        <!-- jpa 依赖，持久化操作依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
```
一个用于链接数据库，用个用于持久化操作

参考：  
https://dzone.com/articles/using-mysql-jdbc-driver-with-spring-boot

3. swagger2 集成
注解依赖：
```xml
 <!-- swagger2 注解依赖 -->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.7.0</version>
        </dependency>
```
ui依赖(这个是可选的):
```xml
<!-- swagger2 ui-->
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.7.0</version>
        </dependency>
```

参考：   
https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api













### 问题：

1. 如果出现如下错误：

```
Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.

Thu Nov 29 14:45:37 CST 2018
There was an unexpected error (type=Not Found, status=404).
No message available
```
可能的原因有：  
[1]  
Application启动类的位置不对.要将Application类放在最外侧,即包含所有子包

spring-boot会自动加载启动类所在包下及其子包下的所有组件    

[2]  
控制器的URL路径书写问题：
类名上添加：@RestController
@RequestMapping(“xxxxxxxxxxxxxx”) ，实际访问的路径与”xxx”不符合