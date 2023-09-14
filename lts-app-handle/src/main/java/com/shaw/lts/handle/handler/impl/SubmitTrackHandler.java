package com.shaw.lts.handle.handler.impl;

import com.shaw.lts.adapter.service.LtsUserTrackService;
import com.shaw.lts.handle.demain.ApiInput;
import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.demain.dto.TrackInfoDto;
import com.shaw.lts.handle.handler.AbstractApiHandler;
import com.shaw.lts.handle.utils.DateUtils;
import com.shaw.lts.persist.model.LtsUserTrack;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * SubmitTrackHandler
 * 上报位置信息 handler
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/24 17:46
 **/
@Component
public class SubmitTrackHandler extends AbstractApiHandler<TrackInfoDto, Void> {

    private final LtsUserTrackService userTrackService;

    public SubmitTrackHandler(LtsUserTrackService userTrackService) {
        this.userTrackService = userTrackService;
    }

    @Override
    public ApiOutput<Void> handle(ApiInput<TrackInfoDto> input) {
        TrackInfoDto trackInfoDto = input.getData();
        LtsUserTrack userTrack = new LtsUserTrack();
        BeanUtils.copyProperties(trackInfoDto, userTrack, "trackTime");
        userTrack.setUserId(this.getLoginUser().getUserId());
        userTrack.setTrackTime(DateUtils.stringToDate(trackInfoDto.getTrackTime(), "yyyy-MM-dd HH:mm:ss"));
        userTrack.setCreateTime(new Date());
        userTrackService.save(userTrack);
        return ApiOutput.ok();
    }
}
