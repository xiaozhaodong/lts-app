package com.shaw.lts.handle.handler.impl;

import com.shaw.lts.adapter.service.LtsUserCareService;
import com.shaw.lts.handle.demain.ApiInput;
import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.demain.LoginUser;
import com.shaw.lts.handle.demain.dto.EditCareUserDto;
import com.shaw.lts.handle.exception.BusinessException;
import com.shaw.lts.handle.handler.AbstractApiHandler;
import com.shaw.lts.persist.model.LtsUserCare;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * EditCareUserHandler
 * 编辑关心的人
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/26 15:16
 **/
@Component
public class EditCareUserHandler extends AbstractApiHandler<EditCareUserDto, Void> {

    private final LtsUserCareService userCareService;

    public EditCareUserHandler(LtsUserCareService userCareService) {
        this.userCareService = userCareService;
    }

    @Override
    public ApiOutput<Void> handle(ApiInput<EditCareUserDto> input) {
        EditCareUserDto careUserDto = input.getData();
        LoginUser loginUser = this.getLoginUser();
        this.currentProxy(EditCareUserHandler.class).editCare(loginUser.getUserId(), careUserDto);
        return ApiOutput.ok();
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void editCare(String userId, EditCareUserDto careUserDto) {
        LtsUserCare ltsUserCare = userCareService.selectOne(e -> {
            e.setUserId(userId);
            e.setCareUserId(careUserDto.getCareUserId());
        });
        if (ltsUserCare == null) {
            throw new BusinessException("关心的用户不存在");
        }
        ltsUserCare.setNickName(careUserDto.getNickName());
        ltsUserCare.setUpdateTime(new Date());
        userCareService.update(ltsUserCare);
    }
}
