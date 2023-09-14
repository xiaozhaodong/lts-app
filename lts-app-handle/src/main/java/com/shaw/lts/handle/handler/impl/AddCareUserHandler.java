package com.shaw.lts.handle.handler.impl;

import com.shaw.lts.adapter.service.LtsSysUserService;
import com.shaw.lts.adapter.service.LtsUserCareService;
import com.shaw.lts.core.utils.IdGeneratorUtils;
import com.shaw.lts.handle.annotation.RepeatSubmit;
import com.shaw.lts.handle.constant.LtsConst;
import com.shaw.lts.handle.demain.ApiInput;
import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.demain.LoginUser;
import com.shaw.lts.handle.demain.dto.AddCareUserDto;
import com.shaw.lts.handle.exception.BusinessException;
import com.shaw.lts.handle.handler.AbstractApiHandler;
import com.shaw.lts.persist.model.LtsSysUser;
import com.shaw.lts.persist.model.LtsUserCare;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * AddCareUserHandler
 * 添加关心的人
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/24 13:57
 **/
@Component
public class AddCareUserHandler extends AbstractApiHandler<AddCareUserDto, Void> {

    private final LtsSysUserService sysUserService;

    private final LtsUserCareService userCareService;

    public AddCareUserHandler(LtsSysUserService sysUserService, LtsUserCareService userCareService) {
        this.sysUserService = sysUserService;
        this.userCareService = userCareService;
    }

    @Override
    @RepeatSubmit
    public ApiOutput<Void> handle(ApiInput<AddCareUserDto> input) {
        LoginUser loginUser = this.getLoginUser();
        AddCareUserDto careUserDto = input.getData();
        String userId = IdGeneratorUtils.genId();
        this.currentProxy(AddCareUserHandler.class).saveCareData(loginUser, careUserDto, userId);
        return ApiOutput.ok();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveCareData(LoginUser loginUser, AddCareUserDto careUserDto, String userId) {
        LtsSysUser ltsSysUser = sysUserService.selectOne(e -> e.setLoginName(careUserDto.getCarePhoneNum()));
        if (ltsSysUser != null) {
            LtsUserCare ltsUserCare = userCareService.selectOne(e -> {
                e.setUserId(loginUser.getUserId());
                e.setCareUserId(ltsSysUser.getUserId());
            });
            if (ltsUserCare != null) {
                throw new BusinessException("关心的人已经存在");
            }
        } else {
            LtsSysUser sysUser = new LtsSysUser();
            sysUser.setUserId(userId);
            sysUser.setLoginName(careUserDto.getCarePhoneNum());
            sysUser.setBlnVip(LtsConst.N);
            sysUser.setBlnEnable(LtsConst.Y);
            sysUser.setBlnLock(LtsConst.N);
            sysUser.setBlnOnline(LtsConst.N);
            sysUser.setBlnDelete(LtsConst.N);
            sysUser.setCreateTime(new Date());
            sysUserService.save(sysUser);
        }
        LtsUserCare userCare = new LtsUserCare();
        userCare.setUserId(loginUser.getUserId());
        userCare.setCareUserId(userId);
        userCare.setNickName(careUserDto.getNickName());
        userCare.setCreateTime(new Date());
        userCareService.save(userCare);
    }
}
