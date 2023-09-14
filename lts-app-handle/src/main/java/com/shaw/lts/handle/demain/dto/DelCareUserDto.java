package com.shaw.lts.handle.demain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * DelCareUserDto
 * 删除关心的人
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/26 15:27
 **/
@Schema(title = "DelCareUserDto", description = "删除关心的人请求参数")
public class DelCareUserDto implements Serializable {

    private static final long serialVersionUID = -4549506659660693192L;


    @Schema(description = "关心用户ID")
    @NotEmpty(message = "关心用户ID不能为空")
    private String careUserId;

    public String getCareUserId() {
        return careUserId;
    }

    public void setCareUserId(String careUserId) {
        this.careUserId = careUserId;
    }
}
