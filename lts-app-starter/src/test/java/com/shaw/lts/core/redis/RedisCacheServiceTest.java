package com.shaw.lts.core.redis;

import com.shaw.lts.starter.LtsAppApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * RedisCacheServiceTest
 * redis测试
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/24 11:14
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LtsAppApplication.class})
public class RedisCacheServiceTest {

    @Autowired
    private RedisCacheService redisCacheService;

    @Test
    public void executeWithLock() {
        Object obj = redisCacheService.executeWithLock("xiaozhaodong", 300, () -> {
            System.out.println("进来了");
            return new Object();
        });
        System.out.println(obj);
    }
}
