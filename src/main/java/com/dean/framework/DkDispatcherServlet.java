package com.dean.framework;

import com.dean.framework.core.annotation.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author fuhw/Dean
 * @date 2018-10-29
 */
public class DkDispatcherServlet extends HttpServlet {

    private static final String EMPTY_STRING = "";
    private static final String SLASH_STRING = "/";
    private static final String SPOT_STRING = ".";

    private static Properties contextConfig = new Properties();
    private static List<String> classNames = new ArrayList<>();
    private static Map<String, Object> ioc = new HashMap<>();
    private static List<Handler> handlerMapping = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatcher(req, resp);
        } catch (Exception e) {
            resp.getWriter().write("500 SERVER ERROR");
            e.printStackTrace();
        }
    }

    private void doDispatcher(HttpServletRequest req, HttpServletResponse resp) throws InvocationTargetException, IllegalAccessException, IOException {
        Handler handler = retrieveHandler(req);
        if (handler == null) {
            resp.getWriter().write("404 NOT FOUND");
            return;
        }

        Object[] paramValues = new Object[handler.method.getParameterTypes().length];
        // String[]可能多个参数，例如：?name=tom&name=jaine
        Map<String, String[]> reqParams = req.getParameterMap();
        for (Map.Entry<String, String[]> stringEntry : reqParams.entrySet()) {
            String paramName = stringEntry.getKey();
            if (!handler.paramsIndexMapping.keySet().contains(paramName)) {
                continue;
            }

            int paramIndex = handler.paramsIndexMapping.get(paramName);
            paramValues[paramIndex] = Arrays.toString(stringEntry.getValue()).replaceAll("\\[|\\]", EMPTY_STRING);
        }

        int respIndex = handler.paramsIndexMapping.get(HttpServletResponse.class.getName());
        int reqIndex = handler.paramsIndexMapping.get(HttpServletRequest.class.getName());
        paramValues[reqIndex] = req;
        paramValues[respIndex] = resp;

        handler.method.invoke(handler.controller, paramValues);

    }

    private Handler retrieveHandler(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        requestURI = requestURI.replace(req.getContextPath(), EMPTY_STRING);
        for (Handler handler : handlerMapping) {
            Matcher matcher = handler.pattern.matcher(requestURI);
            if (!matcher.matches()) {
                continue;
            }
            return handler;
        }
        return null;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        // 1 加载配置文件
        doLoadConfig(config.getInitParameter("contextConfigLocation"));

        // 2 扫描配置包路径
        doScanner(contextConfig.getProperty("scanPackage"));

        // 3 反射实例化加载到IOC容器中
        doInstance();

        // 4 DI依赖注入，针对IOC容器中加载到的类，自动对需要赋值的属性进行初始化操作
        doAutowired();

        // 5 初始化HandlerMapping
        initHandlerMapping();

    }

    private void initHandlerMapping() {
        if (ioc.isEmpty()) return;
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> clazz = entry.getValue().getClass();
            if (!clazz.isAnnotationPresent(DkController.class)) {
                continue;
            }
            String baseUrl = EMPTY_STRING;
            if (clazz.isAnnotationPresent(DkRequestMapping.class)) {
                DkRequestMapping dkRequestMapping = clazz.getAnnotation(DkRequestMapping.class);
                baseUrl = dkRequestMapping.value();
            }

            for (Method method : clazz.getMethods()) {
                if (!method.isAnnotationPresent(DkRequestMapping.class)) {
                    continue;
                }
                DkRequestMapping dkRequestMapping = method.getAnnotation(DkRequestMapping.class);
                String regexUrl = (SLASH_STRING + baseUrl + dkRequestMapping.value()).replaceAll("/+", SLASH_STRING);
                Pattern pattern = Pattern.compile(regexUrl);
                handlerMapping.add(new Handler(method, entry.getValue(), pattern));

                System.out.println("Mapping: [" + regexUrl + "] ==>" + method);
            }
        }
    }

    private void doAutowired() {
        if (ioc.isEmpty()) return;
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Field[] declaredFields = entry.getValue().getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (!declaredField.isAnnotationPresent(DkAutowired.class)) {
                    continue;
                }
                String beanName = getFirstLower(declaredField.getType().getName());
                DkAutowired annotation = declaredField.getAnnotation(DkAutowired.class);
                if (!EMPTY_STRING.equals(annotation.value())) {
                    beanName = annotation.value();
                }
                try {
                    declaredField.setAccessible(true);
                    declaredField.set(entry.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    continue;
                }
            }

        }
    }

    private void doInstance() {
        if (classNames.isEmpty()) return;

        for (String className : classNames) {

            try {
                Class<?> clazz = Class.forName(className);
                if (clazz.isInterface()) continue;
                // 针对指定扫描到的包进行实例化

                // 默认bean名称是类名的首字母小写
                String beanName = getFirstLower(clazz.getName());
                Object newInstance = clazz.newInstance();
                if (clazz.isAnnotationPresent(DkController.class)) {
                    // 指定了bean名称
                    DkController dkController = clazz.getAnnotation(DkController.class);
                    if (!EMPTY_STRING.equals(dkController.value())) {
                        beanName = dkController.value();
                    }

                } else if (clazz.isAnnotationPresent(DkService.class)) {
                    // 指定bean名称
                    DkService dkService = clazz.getAnnotation(DkService.class);
                    if (!EMPTY_STRING.equals(dkService.value())) {
                        beanName = dkService.value();
                    }

                    // 针对接口的，bean名称用接口的名称
                    Class<?>[] interfaces = clazz.getInterfaces();
                    if (interfaces.length == 1) {
                        for (Class<?> anInterface : interfaces) {
                            beanName = getFirstLower(anInterface.getName());
                        }
                    } else if (interfaces.length > 1) {
                        // TODO 多接口
                    }

                } else {
                    continue;
                }
                ioc.put(beanName, newInstance);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private String getFirstLower(String className) {
        String[] strings = className.split("\\.");
        String beanName = strings[strings.length - 1];
        char[] chars = beanName.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }

    private void doScanner(String scanPackage) {
        URL url = this.getClass().getClassLoader().getResource(
                SLASH_STRING + scanPackage.replaceAll("\\.", SLASH_STRING));
        if (url == null) return;
        File classDir = new File(url.getFile());
        for (File file : classDir.listFiles()) {
            if (file.isDirectory()) {
                doScanner(scanPackage + SPOT_STRING + file.getName());
            } else {
                String className = scanPackage + SPOT_STRING +
                        file.getName().replace(".class", EMPTY_STRING);
                classNames.add(className);
            }
        }
    }

    private void doLoadConfig(String contextConfigLocation) {
        contextConfigLocation = contextConfigLocation.replace("classpath:", EMPTY_STRING);
        try (InputStream resourceAsStream = this.getClass().getClassLoader()
                .getResourceAsStream(contextConfigLocation)) {
            contextConfig.load(resourceAsStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    static class Handler {
        private Method method;
        private Object controller;
        private Pattern pattern;
        private Map<String, Integer> paramsIndexMapping;

        public Handler(Method method, Object controller, Pattern pattern) {
            this.method = method;
            this.controller = controller;
            this.pattern = pattern;
            this.paramsIndexMapping = new HashMap<>();
            this.putParamsIndexMapping(method);
        }

        private void putParamsIndexMapping(Method method) {
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameterAnnotations.length; i++) {
                for (Annotation parameterAnnotation : parameterAnnotations[i]) {
                    if (parameterAnnotation instanceof DkRequestParam) {
                        String paramName = ((DkRequestParam) parameterAnnotation).value();
                        paramsIndexMapping.put(paramName, i);
                    }
                }
            }

            // 提取request和response
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> parameterType = parameterTypes[i];
                if (parameterType.equals(HttpServletRequest.class)
                        || parameterType.equals(HttpServletResponse.class)) {
                    paramsIndexMapping.put(parameterType.getName(), i);
                }
            }

        }

    }
}
