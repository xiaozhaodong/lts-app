package com.shaw.lts.core.aop;

import com.shaw.lts.core.config.redis.RedisProperties;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * RedisRootAspect
 * redis全局增加root目录
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2021-01-14 17:29
 **/
@Component
public class RedisRootAspect {


    private final RedisProperties redisProperties;

    public RedisRootAspect(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    public Advisor redisRootAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(public * com.shaw.lts.core.redis.RedisCacheService.*(..))");
        Advice advice = new MethodAroundAdvice();

        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    private class MethodAroundAdvice implements MethodBeforeAdvice {

        @Override
        public void before(@Nullable Method method, Object[] args, Object target) {
            String key = String.valueOf(args[0]);
            args[0] = redisProperties.getRedisRootDir() + ":" + key;
        }
    }
}
