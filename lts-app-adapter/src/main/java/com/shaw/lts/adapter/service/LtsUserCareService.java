package com.shaw.lts.adapter.service;

import com.shaw.lts.adapter.bo.UserCareBo;
import com.shaw.lts.core.service.BaseService;
import com.shaw.lts.persist.model.LtsUserCare;

import java.util.List;

/**
 * LtsUserCareService
 *  Service
 * 
 * @author generate by xzd
 * @version 1.0.0
 * @date Mon Jul 24 13:44:52 CST 2023
*/
public interface LtsUserCareService extends BaseService<LtsUserCare> {

    /**
     * 查询用户关心的人 list
     * @param userId 用户id
     * @param loginName 用户登录名手机号
     * @return {@link List<UserCareBo>}
     */
    List<UserCareBo> selectByUserId(String userId, String loginName);
}