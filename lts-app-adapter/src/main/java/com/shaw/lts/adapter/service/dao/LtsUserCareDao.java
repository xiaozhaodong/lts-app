package com.shaw.lts.adapter.service.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * LtsUserCareDao
 * 用户关心的人dao
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/25 20:21
 **/
public interface LtsUserCareDao {

    /**
     * 查询用户关心的人
     *
     * @param userId 用户 Id
     * @return Map<String, Object>
     */
    @Select({"<script>",
        "SELECT ",
        "a.user_id, ",
        "a.care_user_id, ",
        "a.nick_name, ",
        "b.user_name, ",
        "b.login_name, ",
        "b.bln_online ",
        "FROM lts_user_care a ",
        "INNER JOIN lts_sys_user b ON a.care_user_id = b.user_id ",
        "WHERE a.user_id = #{userId} ",
        "<if test='loginName != null and loginName != \"\"'>AND b.login_name = #{loginName}</if>",
        "</script>"
    })
    List<Map<String, Object>> selectByUserId(@Param("userId") String userId, @Param("loginName") String loginName);
}
