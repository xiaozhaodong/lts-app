package com.shaw.lts.handle.demain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * EditCareUserDto
 * 编辑关心的人 dto
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/26 15:04
 **/
@Schema(title = "EditCareUserDto", description = "编辑关心的人请求参数")
public class EditCareUserDto implements Serializable {

    private static final long serialVersionUID = -4829381038776365696L;

    /**
     * 关心用户 ID
     */
    @Schema(description = "关心用户ID")
    @NotEmpty(message = "关心用户ID不能为空")
    private String careUserId;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    @Length(max = 50, message = "昵称不能超过50个字")
    private String nickName;

    public String getCareUserId() {
        return careUserId;
    }

    public void setCareUserId(String careUserId) {
        this.careUserId = careUserId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
