package com.shaw.lts.adapter.service;

import com.shaw.lts.core.utils.JacksonUtils;
import com.shaw.lts.persist.model.LtsUserTrack;
import com.shaw.lts.starter.LtsAppApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * LtsUserTrackServiceTest
 * 用户追踪表测试
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/25 20:10
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {LtsAppApplication.class})
public class LtsUserTrackServiceTest {

    @Autowired
    private LtsUserTrackService userTrackService;

    @Test
    public void selectLatelyTrack() {
        LtsUserTrack ltsUserTrack = userTrackService.selectLatelyTrack("249518742534795264");
        System.out.println(JacksonUtils.toJson(ltsUserTrack));
    }
}
