package com.shaw.lts.core.config.redis;


import com.shaw.lts.core.redis.RedisCacheService;
import com.shaw.lts.core.redis.impl.RedisClusterServiceImpl;
import com.shaw.lts.core.redis.impl.RedisStandServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.ConnectionPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * RedisConfig
 * redis配置
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2020-12-01 13:55
 **/
@Configuration
public class RedisConfig {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final RedisProperties properties;

    public RedisConfig(RedisProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RedisCacheService getRedisService() {
        if (!properties.isCluster()) {
            return new RedisStandServiceImpl(getRedisPool());
        } else {
            return new RedisClusterServiceImpl(getRedisCluster());
        }
    }

    @Bean
    public JedisPool getRedisPool() {
        if (!properties.isCluster()) {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(properties.getMaxTotal());
            jedisPoolConfig.setMaxIdle(properties.getMaxIdle());
            jedisPoolConfig.setTestOnBorrow(true);
            jedisPoolConfig.setTestOnReturn(true);
            jedisPoolConfig.setJmxEnabled(false);
            logger.info("redis注入.........");
            String[] addresses = properties.getAddress().split(":");
            logger.info("redis单机:{}", properties.getAddress());
            if (StringUtils.isNotEmpty(properties.getPassword())) {
                return new JedisPool(jedisPoolConfig, addresses[0], Integer.parseInt(addresses[1]), properties.getConnectionTimeout(), properties.getPassword());
            } else {
                return new JedisPool(jedisPoolConfig, addresses[0], Integer.parseInt(addresses[1]));
            }
        } else {
            return null;
        }
    }

    @Bean
    public JedisCluster getRedisCluster() {
        if (properties.isCluster()) {
            ConnectionPoolConfig connectionPoolConfig = new ConnectionPoolConfig();
            // 设置最大连接数，默认值为8.如果赋值为-1，则表示不限制；
            connectionPoolConfig.setMaxTotal(properties.getMaxTotal());
            // 最大空闲连接数
            connectionPoolConfig.setMaxIdle(properties.getMaxIdle());
            // 最小空闲连接数
            connectionPoolConfig.setMinIdle(10);
            // 在获取Jedis连接时，自动检验连接是否可用
            connectionPoolConfig.setTestOnBorrow(true);
            // 在将连接放回池中前，自动检验连接是否有效
            connectionPoolConfig.setTestOnReturn(true);
            // 自动测试池中的空闲连接是否都是可用连接
            connectionPoolConfig.setTestWhileIdle(true);
            // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
            connectionPoolConfig.setBlockWhenExhausted(true);
            // 是否启用pool的jmx管理功能, 默认true
            connectionPoolConfig.setJmxEnabled(true);
            // 是否启用后进先出, 默认true
            connectionPoolConfig.setLifo(true);
            // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
            connectionPoolConfig.setNumTestsPerEvictionRun(3);
            logger.info("redis注入.........");
            String[] addresses = properties.getAddress().split(",");
            logger.info("redis集群:{}", Arrays.toString(addresses));
            Set<HostAndPort> hostAndPorts = new HashSet<>();
            for (String host : addresses) {
                String[] hosts = host.split(":");
                HostAndPort hostAndPort = new HostAndPort(hosts[0], Integer.parseInt(hosts[1]));
                hostAndPorts.add(hostAndPort);
            }
            if (StringUtils.isEmpty(properties.getPassword())) {
                return new JedisCluster(hostAndPorts, properties.getConnectionTimeout(), properties.getSoTimeout(),
                    properties.getMaxAttempts(), connectionPoolConfig);
            } else {
                return new JedisCluster(hostAndPorts, properties.getConnectionTimeout(), properties.getSoTimeout(),
                    properties.getMaxAttempts(), properties.getPassword(), connectionPoolConfig);
            }
        }
        return null;
    }
}
