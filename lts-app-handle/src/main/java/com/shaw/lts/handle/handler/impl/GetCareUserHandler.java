package com.shaw.lts.handle.handler.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.shaw.lts.adapter.bo.UserCareBo;
import com.shaw.lts.adapter.service.LtsUserCareService;
import com.shaw.lts.adapter.service.LtsUserTrackService;
import com.shaw.lts.handle.constant.LtsConst;
import com.shaw.lts.handle.demain.ApiInput;
import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.demain.LoginUser;
import com.shaw.lts.handle.demain.PageResult;
import com.shaw.lts.handle.demain.dto.GetCareUserDto;
import com.shaw.lts.handle.demain.vo.TrackVo;
import com.shaw.lts.handle.demain.vo.UserCareVo;
import com.shaw.lts.handle.handler.AbstractApiHandler;
import com.shaw.lts.handle.utils.DateUtils;
import com.shaw.lts.persist.model.LtsUserTrack;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * GetCareUserHandler
 * 获取关心的人 handler
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/25 17:52
 **/
@Component
public class GetCareUserHandler extends AbstractApiHandler<GetCareUserDto, PageResult<UserCareVo>> {

    private final LtsUserCareService userCareService;

    private final LtsUserTrackService userTrackService;

    public GetCareUserHandler(LtsUserCareService userCareService, LtsUserTrackService userTrackService) {
        this.userCareService = userCareService;
        this.userTrackService = userTrackService;
    }

    @Override
    public ApiOutput<PageResult<UserCareVo>> handle(ApiInput<GetCareUserDto> input) {
        GetCareUserDto careUserDto = input.getData();
        LoginUser loginUser = this.getLoginUser();
        String userId = loginUser.getUserId();
        int pageNum = careUserDto.getPageNum();
        int pageSize = careUserDto.getPageSize();
        try (Page<UserCareBo> page = PageHelper.startPage(pageNum, pageSize,
            "a.create_time desc")) {
            List<UserCareBo> userCareBos = userCareService.selectByUserId(userId, careUserDto.getCareUserPhone());
            List<UserCareVo> userCareVos = userCareBos.stream().map(e -> installUserCareVo(loginUser, userId, e))
                .collect(Collectors.toList());
            PageResult<UserCareVo> pageResult = PageResult.build(pageNum, page.getTotal(), userCareVos);
            return ApiOutput.ok(pageResult);
        }
    }

    private UserCareVo installUserCareVo(LoginUser loginUser, String userId, UserCareBo e) {
        UserCareVo userCareVo = new UserCareVo();
        userCareVo.setCareUserId(e.getCareUserId());
        userCareVo.setCareUserPhone(e.getLoginName());
        userCareVo.setNickName(e.getNickName());
        userCareVo.setBlnOline(e.getBlnOnline());
        if (LtsConst.Y.equals(loginUser.getBlnVip())) {
            // 如果是vip这里还需要查询定位信息
            LtsUserTrack userTrack = userTrackService.selectLatelyTrack(userId);
            if (userTrack != null) {
                TrackVo trackVo = new TrackVo();
                BeanUtils.copyProperties(userTrack, trackVo, "trackTime");
                trackVo.setTrackTime(DateUtils.dateToString(userTrack.getTrackTime(), "yyyy-MM-dd HH:mm:ss"));
                userCareVo.setTrackVo(trackVo);
            }
        }
        return userCareVo;
    }
}
