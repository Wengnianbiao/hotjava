package com.java.biao.spring.servlet;

import com.java.biao.spring.annotation.GPController;
import com.java.biao.spring.annotation.GPRequestMapping;
import com.java.biao.spring.context.GPApplicationContext;
import com.java.biao.spring.mvc.GPHandlerAdapter;
import com.java.biao.spring.mvc.GPHandlerMapping;
import com.java.biao.spring.mvc.GPModelAndView;
import com.java.biao.spring.mvc.GPView;
import com.java.biao.spring.mvc.GPViewResolver;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@NoArgsConstructor
public class GPDispatcherServlet extends HttpServlet {


    private final String LOCATION = "contextConfigLocation";

    private GPApplicationContext applicationContext;
    private List<GPHandlerMapping> handlerMapping = new ArrayList<>();

    private Map<GPHandlerMapping, GPHandlerAdapter> handlerAdapters = new HashMap<>();

    private List<GPViewResolver> viewResolvers = new ArrayList<>();

    @Override
    public void init(ServletConfig config) {
        // 初始化ioc容器,MVC的前提就是已经把容器初始化完毕，即把相关的组件都已经放置到容器中了
        applicationContext = new GPApplicationContext(config.getInitParameter(LOCATION));
        // 初始化各种策略，本质是根据注解来解析成key-value的映射关系
        initStrategies(applicationContext);
    }

    private void initStrategies(GPApplicationContext applicationContext) {
        // 文件上传解析，针对请求类型是multipart/form-data
        initMultipartResolver(applicationContext);
        // 国际化，针对请求语言
        initLocaleResolver(applicationContext);
        // 主题解析，针对请求主题
        initThemeResolver(applicationContext);
        // 最重要的请求映射处理器
        initHandlerMappings(applicationContext);
        // 请求参数解析器，针对请求参数进行动态适配
        initHandlerAdapters(applicationContext);
        // 错误解析器，针对请求错误
        initHandlerExceptionResolvers(applicationContext);
        // 请求映射到视图名称解析器，针对请求映射到视图名称
        initRequestToViewNameTranslator(applicationContext);
        // 逻辑视图解析到具体视图解析器
        initViewResolvers(applicationContext);
        // flashMap管理器,不知道是什么的,不过从名称看估计和Flash相关
        initFlashMapManager(applicationContext);
    }

    private void initFlashMapManager(GPApplicationContext applicationContext) {
    }

    private void initViewResolvers(GPApplicationContext applicationContext) {
        // ViewResolver也算一种策略，根据不同的请求选择不同的模板引擎来进行页面的渲染
        String templateRoot = applicationContext.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootDir = new File(templateRootPath);
        String[] templates = templateRootDir.list();
        for (int i = 0; i < templates.length; i ++) {
            //这里主要是为了兼容多模板，所有模仿Spring用List保存
            //在我写的代码中简化了，其实只有需要一个模板就可以搞定
            //只是为了仿真，所有还是搞了个List
            this.viewResolvers.add(new GPViewResolver(templateRoot));
        }
    }

    private void initRequestToViewNameTranslator(GPApplicationContext applicationContext) {
    }

    private void initHandlerExceptionResolvers(GPApplicationContext applicationContext) {
    }

    private void initHandlerAdapters(GPApplicationContext applicationContext) {
        // 将Request的字符型参数自动适配为Method的Java实参
        for (GPHandlerMapping handlerMapping : this.handlerMapping) {
            this.handlerAdapters.put(handlerMapping, new GPHandlerAdapter());
        }

    }

    private void initHandlerMappings(GPApplicationContext applicationContext) {
        // 映射不同的url到对应的处理器
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        try {
            for (String beanName : beanNames) {
                Object controller = applicationContext.getBean(beanName);
                if (controller == null) {
                    continue;
                }
                Class<?> clazz = controller.getClass();
                // 只处理 GPController注解的类
                if (!clazz.isAnnotationPresent(GPController.class)) {
                    continue;
                }
                String baseUrl = "";
                if (clazz.isAnnotationPresent(GPRequestMapping.class)) {
                    GPRequestMapping requestMapping = clazz.getAnnotation(GPRequestMapping.class);
                    baseUrl = requestMapping.value();
                }

                // 扫描当前controller类下所有的public方法后进行注册
                Method[] methods = clazz.getMethods();
                for (Method method : methods) {
                    if (!method.isAnnotationPresent(GPRequestMapping.class)) {
                        continue;
                    }

                    GPRequestMapping requestMapping = method.getAnnotation(GPRequestMapping.class);
                    String regex = ("/" + baseUrl + "/" + requestMapping.value())
                            // 应当是匹配所有，所以用.*
                            .replaceAll("\\*", ".*")
                            // 将多个/换成一个/
                            .replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(regex);
                    // 将url和method的正则表达式进行映射
                    this.handlerMapping.add(new GPHandlerMapping(controller, method, pattern));
                    log.info("having register mapping:" + regex + "->" + method);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initThemeResolver(GPApplicationContext applicationContext) {
    }

    private void initLocaleResolver(GPApplicationContext applicationContext) {
    }

    private void initMultipartResolver(GPApplicationContext applicationContext) {
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // servlet容器会将http请求到到达的doGet方法，所以这里重写doGet方法，调用doPost方法
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //1、通过从request中拿到URL，去匹配一个HandlerMapping
        GPHandlerMapping handler = getHandler(req);

        if (handler == null) {
            processDispatchResult(req, resp, new GPModelAndView("404"));
        }
        //2、准备调用前的参数
        GPHandlerAdapter handlerAdapter = getHandlerAdapter(handler);

        //3、真正的调用方法,返回ModelAndView存储了要穿页面上值，和页面模板的名称
        GPModelAndView mv = handlerAdapter.handle(req, resp, handler);

        //4、真正的输出
        processDispatchResult(req, resp, mv);
    }

    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, GPModelAndView modelAndView) throws Exception {
        if (modelAndView == null) {
            return;
        }
        if (!this.viewResolvers.isEmpty()) {
            for (GPViewResolver viewResolver : this.viewResolvers) {
                GPView view = viewResolver.resolveViewName(modelAndView.getViewName(), null);
                if (view != null) {
                    view.render(modelAndView.getModel(), req, resp);
                    return;
                }
            }
        }


    }

    private GPHandlerMapping getHandler(HttpServletRequest req) {
        if (this.handlerMapping.isEmpty()) {
            return null;
        }
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");
        for (GPHandlerMapping handler : this.handlerMapping) {
            Matcher matcher = handler.getPattern().matcher(url);
            if (!matcher.matches()) {
                continue;
            }
            return handler;
        }
        return null;
    }

    private GPHandlerAdapter getHandlerAdapter(GPHandlerMapping handlerMapping) {
        if (this.handlerAdapters.isEmpty()) {
            return null;
        }
        GPHandlerAdapter handlerAdapter = this.handlerAdapters.get(handlerMapping);
        if (handlerAdapter.supports(handlerMapping)) {
            return handlerAdapter;
        }
        return null;
    }
}
