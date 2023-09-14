package com.shaw.lts.adapter.service.impl;

import com.shaw.lts.adapter.bo.UserCareBo;
import com.shaw.lts.adapter.service.LtsUserCareService;
import com.shaw.lts.adapter.service.dao.LtsUserCareDao;
import com.shaw.lts.adapter.utils.SelectUtils;
import com.shaw.lts.core.service.impl.BaseServiceImpl;
import com.shaw.lts.persist.model.LtsUserCare;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * LtsUserCareService
 * Impl
 *
 * @author generate by xzd
 * @version 1.0.0
 * @date Mon Jul 24 13:44:52 CST 2023
 */
@Service
public class LtsUserCareServiceImpl extends BaseServiceImpl<LtsUserCare> implements LtsUserCareService {

    @Resource
    private LtsUserCareDao ltsUserCareDao;

    @Override
    public List<UserCareBo> selectByUserId(String userId, String loginName) {
        List<Map<String, Object>> resultMaps = ltsUserCareDao.selectByUserId(userId, loginName);
        return resultMaps.stream().map(e -> SelectUtils.toPojo(UserCareBo.class, e)).collect(Collectors.toList());
    }
}