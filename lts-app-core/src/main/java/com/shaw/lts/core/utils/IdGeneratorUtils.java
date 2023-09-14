package com.shaw.lts.core.utils;

import com.shaw.lts.core.redis.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;


/**
 * id生成组件
 * @author xiaozhaodong
 */
@Component
public class IdGeneratorUtils {

    private static final Logger logger = LoggerFactory.getLogger(IdGeneratorUtils.class);

    private static IdWorker idWorker;

    private final RedisCacheService redisCacheService;

    public IdGeneratorUtils(RedisCacheService redisCacheService) {
        this.redisCacheService = redisCacheService;
    }

    @PostConstruct
    public void init() {
        logger.info("初始化雪花算法类");
        long workerId = redisCacheService.increment("workerId", 1L);
        redisCacheService.expire("workerId", 60 * 60);
        workerId %= 1024L;
        long dataCenterId = redisCacheService.increment("dataCenterId", 1L);
        redisCacheService.expire( "dataCenterId", 60 * 60);
        dataCenterId %= 1024L;
        logger.info("初始化SnowFlake workerId:{},dataCenterId:{}", workerId, dataCenterId);
        idWorker = new IdWorker(workerId, dataCenterId);
    }

    /**
     * 获取一个雪花算法ID
     *
     * @return String
     */
    public static String genId() {
        long nextId = idWorker.nextId();
        return String.valueOf(nextId);
    }

    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
