package com.shaw.lts.adapter.service.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

/**
 * LtsUserTrackDao
 * 用户追踪表 dao
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/25 20:02
 **/
public interface LtsUserTrackDao {

    @Select("select * from lts_user_track where user_id = #{userId} order by track_time desc limit 1")
    Map<String, Object> selectLatelyTrack(@Param("userId") String userId);
}
