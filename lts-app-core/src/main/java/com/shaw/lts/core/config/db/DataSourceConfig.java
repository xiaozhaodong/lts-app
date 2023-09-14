package com.shaw.lts.core.config.db;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.github.pagehelper.PageInterceptor;
import com.shaw.lts.core.aop.SqlStatementInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * DataSourceConfig
 * 数据源配置
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2020-12-01 13:48
 **/
@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    private final Environment env;

    private final DruidDataSourceProperties properties;

    public DataSourceConfig(Environment env, DruidDataSourceProperties properties) {
        this.env = env;
        this.properties = properties;
    }

    @Bean(name = "dataSource")
    public DataSource druidDataSource() throws SQLException {
        return DruidDataSourceBuilder.build(properties.getDriverClassName(), properties.getUrl(),
            properties.getUsername(), properties.getPassword(), properties.getInitialSize(),
            properties.getMinIdle(), properties.getMaxActive(), properties.getMaxWait(),
            properties.getTimeBetweenEvictionRunsMillis(), properties.getMinEvictableIdleTimeMillis(),
            properties.getValidationQuery(), properties.isTestWhileIdle(), properties.isTestOnBorrow(),
            properties.isTestOnReturn(), properties.isPoolPreparedStatements(),
            properties.getMaxPoolPreparedStatementPerConnectionSize(), properties.isDecrypt(),
            properties.getDecryptKey());
    }

    @Bean
    public DataSourceTransactionManager myTransactionManager() throws SQLException {
        return new DataSourceTransactionManager(druidDataSource());
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(dataSource);
        fb.setPlugins(new Interceptor[]{getPageInterceptor(), new SqlStatementInterceptor()});
//        fb.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(
//            Objects.requireNonNull(env.getProperty("mybatis.mapper-locations"))));
        fb.setConfigLocation(new PathMatchingResourcePatternResolver().getResource(
            Objects.requireNonNull(env.getProperty("mybatis.config-location"))));
        return fb.getObject();
    }

    @Bean
    public PageInterceptor getPageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        final Properties prop = new Properties();
        prop.put("helperDialect", "mysql");
        prop.put("reasonable", "true");
        pageInterceptor.setProperties(prop);
        return pageInterceptor;
    }

    @Bean
    public ServletRegistrationBean<Servlet> druidStatViewServlet() {
        ServletRegistrationBean<Servlet> servletRegistrationBean = new ServletRegistrationBean<>(new StatViewServlet(),
            "/druid/*");
        Map<String, String> initParams = new HashMap<>(2);
        // 可配的属性都在 StatViewServlet 和其父类下
        initParams.put("loginUsername", "druid");
        initParams.put("loginPassword", "druid");
        servletRegistrationBean.setInitParameters(initParams);
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<Filter> druidWebStatFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>(new WebStatFilter());
        Map<String, String> initParams = new HashMap<>(2);
        initParams.put("exclusions", "*.js,*.css,/druid/*");
        filterRegistrationBean.setInitParameters(initParams);
        filterRegistrationBean.setUrlPatterns(Collections.singletonList("/*"));
        return filterRegistrationBean;
    }
}
