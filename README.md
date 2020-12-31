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

##### Step7 验证功能

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


## 一 针对循环依赖问题的验证：

```asp
ServiceA跟ServiceB相互依赖问题：

ServiceA
 -serviceB

ServiceB
 -ServiceA
```
<image src="static/img/service-call.png" width="400px" height="250px"></image>



## 唠唠嗑

| 联系我 | 下午茶(支付宝) |  下午茶(微信)|
| :------: | :------: | :------: |
| <img src="https://note.youdao.com/yws/api/personal/file/WEB87ca0fa2c0ee3e9f6c32fe5523f88c88?method=download&shareKey=907b187d00e41a0128f13e575ddf7f10" width="200"> |<img src="https://note.youdao.com/yws/api/personal/file/WEB143ed930a3562f2e65442a3f5b0e7bdd?method=download&shareKey=7a6847f4a2a61ee61522cf3ae7324846" width="200"> | <img src="https://note.youdao.com/yws/api/personal/file/WEBcc0561b27e1f96f089d624af2ad710ed?method=download&shareKey=b6ada6ef8e555407a34fd19f238eba0b" width="200">|  