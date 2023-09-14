package com.shaw.lts.core.config.db;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DruidDataSourceBuilder
 * druid数据源创建帮助类
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2022/6/8 11:34
 **/
public class DruidDataSourceBuilder {

    public static DataSource build(String driverClassName, String url, String username,
        String password, int initialSize, int minIdle,
        int maxActive, long maxWait, long timeBetweenEvictionRunsMillis,
        long minEvictableIdleTimeMillis, String validationQuery,
        boolean testWhileIdle, boolean testOnBorrow, boolean testOnReturn,
        boolean poolPreparedStatements, int maxPoolPreparedStatementPerConnectionSize,
        boolean decrypt, String decryptKey) throws SQLException {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMinIdle(minIdle);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setMaxWait(maxWait);
        druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        druidDataSource.setValidationQuery(validationQuery);
        druidDataSource.setTestWhileIdle(testWhileIdle);
        druidDataSource.setTestOnBorrow(testOnBorrow);
        druidDataSource.setTestOnReturn(testOnReturn);
        druidDataSource.setPoolPreparedStatements(poolPreparedStatements);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        druidDataSource.addFilters("stat");
        druidDataSource.addFilters("wall");
        if (decrypt) {
            Properties connectProperties = new Properties();
            connectProperties.setProperty("password", password);
            connectProperties.setProperty("decryptKey", decryptKey);
            druidDataSource.setConnectProperties(connectProperties);
            druidDataSource.setPasswordCallback(new DruidPasswordCallback());
        }
        return druidDataSource;
    }
}
