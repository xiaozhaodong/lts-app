package com.shaw.lts.handle.demain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * UserCareVo
 * 用户关心的人
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/24 17:12
 **/
@Schema(title = "UserCareVo", description = "用户关心的人信息")
public class UserCareVo implements Serializable {

    private static final long serialVersionUID = -5442223965376162334L;

    /**
     * 关心的用户 ID
     */
    @Schema(description = "关心的用户ID")
    private String careUserId;

    /**
     * 关心的用户手机号
     */
    @Schema(description = "关心的用户手机号")
    private String careUserPhone;

    /**
     * 关心用户的昵称
     */
    @Schema(description = "关心用户的昵称")
    private String nickName;

    /**
     * 在线状态 1 在线 0 离线
     */
    @Schema(description = "在线状态1:在线 0:离线")
    private String blnOline;

    /**
     * 用户位置信息
     */
    @Schema(description = "关心用户的位置信息")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TrackVo trackVo;

    public String getCareUserId() {
        return careUserId;
    }

    public void setCareUserId(String careUserId) {
        this.careUserId = careUserId;
    }

    public String getCareUserPhone() {
        return careUserPhone;
    }

    public void setCareUserPhone(String careUserPhone) {
        this.careUserPhone = careUserPhone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getBlnOline() {
        return blnOline;
    }

    public void setBlnOline(String blnOline) {
        this.blnOline = blnOline;
    }

    public TrackVo getTrackVo() {
        return trackVo;
    }

    public void setTrackVo(TrackVo trackVo) {
        this.trackVo = trackVo;
    }
}
