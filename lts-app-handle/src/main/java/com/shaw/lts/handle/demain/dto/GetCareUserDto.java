package com.shaw.lts.handle.demain.dto;

import com.shaw.lts.handle.demain.Page;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * GetCareUserDto
 * 获取关心的人 dto
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/25 19:39
 **/
@Schema(title = "GetCareUserDto", description = "添获取关心的人请求参数")
public class GetCareUserDto extends Page implements Serializable {

    private static final long serialVersionUID = 2901335374414754002L;

    /**
     * 关心的人手机号码
     */
    @Schema(description = "关心的人手机号码")
    private String careUserPhone;

    public String getCareUserPhone() {
        return careUserPhone;
    }

    public void setCareUserPhone(String careUserPhone) {
        this.careUserPhone = careUserPhone;
    }
}
