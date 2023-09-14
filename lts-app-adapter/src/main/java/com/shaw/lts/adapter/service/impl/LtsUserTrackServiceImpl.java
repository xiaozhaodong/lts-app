package com.shaw.lts.adapter.service.impl;

import com.shaw.lts.adapter.service.LtsUserTrackService;
import com.shaw.lts.adapter.service.dao.LtsUserTrackDao;
import com.shaw.lts.adapter.utils.SelectUtils;
import com.shaw.lts.core.service.impl.BaseServiceImpl;
import com.shaw.lts.persist.model.LtsUserTrack;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Map;

/**
 * LtsUserTrackService
 *  Impl
 * 
 * @author generate by xzd
 * @version 1.0.0
 * @date Mon Jul 24 17:24:55 CST 2023
*/
@Service
public class LtsUserTrackServiceImpl extends BaseServiceImpl<LtsUserTrack> implements LtsUserTrackService {

    @Resource
    private LtsUserTrackDao ltsUserTrackDao;

    @Override
    public LtsUserTrack selectLatelyTrack(String userId) {
        Map<String, Object> resultMap = ltsUserTrackDao.selectLatelyTrack(userId);
        return SelectUtils.toPojo(LtsUserTrack.class, resultMap);
    }
}