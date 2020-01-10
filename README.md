## 简单设计实践spring框架（手写spring）

`@author DeanKano`

## 核心流程：


##### Step1 配置依赖（仅仅依赖servlet-api包）

```asp

<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>servlet-api</artifactId>
    <version>2.4</version>
</dependency>

```

##### Step2 定义一个核心控制器

> DkDispatcherServlet extends HttpServlet {...}

`在web.xml配置核心控制器`

```asp

 <!-- 核心控制器 -->
    <servlet>
        <servlet-name>dkDispatcher</servlet-name>
        <servlet-class>com.dean.framework.DkDispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:application.properties</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>
    <servlet-mapping>
        <servlet-name>dkDispatcher</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>


```


##### Step3 自定义一些类spring的注解

> @DkController

> @DkService

> @DkAutowired

> @DkRequestMapping

> @DkRequestParam

##### Step4 配置容器启动时的初始化

> 1 加载配置文件  doLoadConfig(..)

> 2 扫描配置包路径  doScanner(..)

> 3 反射实例化加载到IOC容器中 doInstance();

> 4 DI依赖注入，针对IOC容器中加载到的类，自动对需要赋值的属性进行初始化操作 doAutowired();

> 5 初始化HandlerMapping initHandlerMapping();

##### Step5 执行请求，请求分发
> doDispatcher(req, resp);

##### Step6 启动

```asp

一 直接运行maven 插件 jetty:run

二 命令行启动，在cmd窗口执行：

  `$mvn jetty:run`

```

##### Step6 验证功能

启动日志：

```asp
...
[INFO] Starting jetty 6.1.7 ...
[INFO] jetty-6.1.7
[INFO] No Transaction manager found - if your webapp requires one, please configure one.
Mapping: [/sample/query.do] ==>public void com.dean.framework.sample.UserAction.query(java.lang.String,javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)
Mapping: [/sample/add.do] ==>public void com.dean.framework.sample.UserAction.addUser(java.lang.String,javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)
Mapping: [/sample/remove.do] ==>public void com.dean.framework.sample.UserAction.removeUser(java.lang.String,javax.servlet.http.HttpServletRequest,javax.servlet.http.HttpServletResponse)
[INFO] Started SelectChannelConnector@0.0.0.0:8080
[INFO] Started Jetty Server

```

请求示例：

`http://localhost:8080/sample/query.do?name=Tom`


##### 唠唠嗑🐧(企鹅群)

<image src="static/img/qq-chat.jpg" width="200px" height="250px"></image>

##### 下午茶🍵(支付宝)

<image src="static/img/zhifubao-pay.png" width="200px" height="200px"></image>
