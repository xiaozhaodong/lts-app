package com.shaw.lts.core.aop;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * SqlStatementInterceptor
 * mysql查询耗时
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/6/5 09:44
 **/
@Intercepts({
    @Signature(type = Executor.class, method = "update", args = {MappedStatement.class,
        Object.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class,
        Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class,
        Object.class, RowBounds.class, ResultHandler.class}),
//    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class SqlStatementInterceptor implements Interceptor {
    public static final Logger log = LoggerFactory.getLogger(SqlStatementInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return invocation.proceed();
        } finally {
            long timeConsuming = System.currentTimeMillis() - startTime;
//            log.info("执行SQL:{}ms", timeConsuming);
            if (timeConsuming > 999 && timeConsuming < 5000) {
                printSql(invocation);
                log.info("执行SQL大于1s:{}ms", timeConsuming);
            } else if (timeConsuming >= 5000 && timeConsuming < 10000) {
                log.info("执行SQL大于5s:{}ms", timeConsuming);
            } else if (timeConsuming >= 10000) {
                log.info("执行SQL大于10s:{}ms", timeConsuming);
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private void printSql(Invocation invocation) {
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
        String sql = boundSql.getSql();
        log.info("执行的SQL语句：{}", sql);
    }

}
