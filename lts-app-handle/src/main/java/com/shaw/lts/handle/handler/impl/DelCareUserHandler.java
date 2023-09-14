package com.shaw.lts.handle.handler.impl;

import com.shaw.lts.adapter.service.LtsUserCareService;
import com.shaw.lts.handle.demain.ApiInput;
import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.demain.LoginUser;
import com.shaw.lts.handle.demain.dto.DelCareUserDto;
import com.shaw.lts.handle.exception.BusinessException;
import com.shaw.lts.handle.handler.AbstractApiHandler;
import com.shaw.lts.persist.model.LtsUserCare;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * DelCareUserHandler
 * 删除关心的人 handler
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/26 15:30
 **/
@Component
public class DelCareUserHandler extends AbstractApiHandler<DelCareUserDto, Void> {

    private final LtsUserCareService userCareService;

    public DelCareUserHandler(LtsUserCareService userCareService) {
        this.userCareService = userCareService;
    }

    @Override
    public ApiOutput<Void> handle(ApiInput<DelCareUserDto> input) {
        DelCareUserDto careUserDto = input.getData();
        LoginUser loginUser = this.getLoginUser();
        this.currentProxy(DelCareUserHandler.class).delCare(loginUser.getUserId(), careUserDto.getCareUserId());
        return ApiOutput.ok();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void delCare(String userId, String careUserId) {
        LtsUserCare ltsUserCare = userCareService.selectOne(e -> {
            e.setUserId(userId);
            e.setCareUserId(careUserId);
        });
        if (ltsUserCare == null) {
            throw new BusinessException("需要删除的人不存在");
        }
        userCareService.deleteByPrimaryKey(ltsUserCare.getId());
    }
}
