package com.shaw.lts.handle.handler;

import com.shaw.lts.core.utils.SpringContextUtil;
import com.shaw.lts.handle.demain.ApiInput;
import com.shaw.lts.handle.demain.ApiOutput;
import org.springframework.stereotype.Component;

/**
 * ApiServiceImpl
 * 服务实现委派给 handler
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/6/16 10:09
 **/
@Component
public class ApiServiceImpl {

//    private final Map<String, Class<? extends AbstractServiceHandler<?, ?>>> methodHandlerMap =
//        new ConcurrentHashMap<>();
//
//    @PostConstruct
//    public void init() {
//        // 扫描指定包路径下的所有类
//        String basePackage = "com.newland.llas.core.controller";
//        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
//        scanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
//        Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(basePackage);
//        for (BeanDefinition beanDefinition : beanDefinitions) {
//            try {
//                Class<?> controllerClass = Class.forName(beanDefinition.getBeanClassName());
//                String controllerClassName = controllerClass.getName();
//                Method[] methods = controllerClass.getMethods();
//                for (Method method : methods) {
//                    DelegateHandler delegateHandler = method.getAnnotation(DelegateHandler.class);
//                    if (delegateHandler != null) {
//                        String methodSignature = controllerClassName + "." + method.getName();
//                        if (methodHandlerMap.containsKey(methodSignature)) {
//                            // 约定 controller 中 被DelegateHandler注解的方法 不允许方法重载，否则报错
//                            throw new RuntimeException("controller中有相同签名的方法");
//                        }
//                        methodHandlerMap.put(methodSignature, delegateHandler.handler());
//                    }
//                }
//            } catch (ClassNotFoundException e) {
//                // 处理异常
//                throw new RuntimeException(e.getMessage());
//            }
//        }
//    }

    @SuppressWarnings("unchecked")
    public <I, O> ApiOutput<O> delegate(ApiInput<I> apiInput) {
        AbstractApiHandler<I, O> handler = (AbstractApiHandler<I, O>) SpringContextUtil.getBean(
            DelegateThreadLocal.getHandlerClass());
        return handler.handle(apiInput);
    }
}

