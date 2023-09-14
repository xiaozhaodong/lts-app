package com.shaw.lts.adapter.service;

import com.shaw.lts.core.service.BaseService;
import com.shaw.lts.persist.model.LtsUserTrack;

/**
 * LtsUserTrackService
 *  Service
 * 
 * @author generate by xzd
 * @version 1.0.0
 * @date Mon Jul 24 17:24:55 CST 2023
*/
public interface LtsUserTrackService extends BaseService<LtsUserTrack> {

    /**
     * 查询最近一条定位信息
     * @param userId 用户 ID
     * @return {@link LtsUserTrack}
     */
    LtsUserTrack selectLatelyTrack(String userId);
}