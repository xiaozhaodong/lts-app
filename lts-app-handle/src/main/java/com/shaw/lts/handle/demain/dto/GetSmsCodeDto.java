package com.shaw.lts.handle.demain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * GetSmsCodeDto
 * 获取短信验证码 dto
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/21 16:21
 **/
@Schema(title = "GetSmsCodeDto", description = "获取短信验证码请求参数")
public class GetSmsCodeDto implements Serializable {

    private static final long serialVersionUID = -3753539095578771569L;

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    @NotEmpty(message = "手机号不能为空")
    @Pattern(regexp = "^(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|" +
        "(?:9[1589]))\\d{8}$", message = "联系人手机号格式不正确")
    private String phoneNum;

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
