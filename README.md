# SjtusimpleNLIOT
NLIOT项目说明
开发环境配置
Intellij + Spring Boot

    下载安装最新版本的Intellij IDEA以及Java JDK 1.8
    从github上Clone一份SjtusimpleNLIOT项目代码到本地,推荐使用Source Tree或其他资源管理软件
    打开Intellij，import project，选择本地项目SjtusimpleNLIOT中的pom.xml

MQTT服务器

    下载安装MQTT服务器，并学会如何启动

Neo4J图数据库

    安装部署Neo4J图数据库

node.js + react

    安装node.js环境,并在cmd环境的frontene目录下启用npm start确认npm是否可用

相关技术
JAVA+Spring Boot

    学会JAVA的基本语法和类的使用方法
    掌握SSH（Spring + Struts +Hibernate）基本框架的思想
    掌握Spring Boot框架的相关技术，尤其是控制反转

JavaScript+React+antd

    学会JavaScript的基本语法，推荐使用ES6版本
    掌握React框架
    掌握AntD组件

代码结构
Java代码

    Java代码在src/main/java目录下，其中结构与SSH框架类似
    entity层为实体类，与数据库中的实体进行映射
    dao层为基础的增删改查功能
    service层为具体功能服务的接口
    controller层为WebService接口的实现
    MQTT目录下为MQTT客户端订阅/发布功能的实现
    启动项为NliotApplication

JavaScript代码

    JS代码在frontene/src目录下，主要为js文件和css文件
    所有代码集中在index.js中

代码中存在的问题
JS代码

    物关TreeNode组件均为hard code，其自动生成的函数getDataF存在bug，需要调试
    请求通信得到物功能表的函数getFunctionTableURL需要后端支持
    state中的固有参数需要整理
    物功能表的展示界面有三种，可以根据需要进行更改

Java代码

    MQTTController中需要符合逻辑的订阅/发布的持续化功能
    MQTT的service需要整合
    其他一些数据库中的业务逻辑需要补充，例如注册，删除等

可能存在更多bug或垃圾代码
