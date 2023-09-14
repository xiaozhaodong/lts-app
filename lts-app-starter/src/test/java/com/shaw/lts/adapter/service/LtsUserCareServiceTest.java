package com.shaw.lts.adapter.service;

import com.shaw.lts.adapter.bo.UserCareBo;
import com.shaw.lts.core.utils.JacksonUtils;
import com.shaw.lts.starter.LtsAppApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * LtsUserCareServiceTest
 * 用户关心的人查询测试
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/26 10:23
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LtsAppApplication.class})
public class LtsUserCareServiceTest {

    @Autowired
    private LtsUserCareService ltsUserCareService;

    @Test
    public void selectByUserId() {
        List<UserCareBo> userCareBos = ltsUserCareService.selectByUserId("249518742534795264", "");
        System.out.println(JacksonUtils.toJson(userCareBos));
    }
}
