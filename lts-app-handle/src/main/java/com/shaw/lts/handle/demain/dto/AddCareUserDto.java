package com.shaw.lts.handle.demain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * AddCareUserDto
 * 添加关心的人dto
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/24 13:49
 **/
@Schema(title = "AddCareUserDto", description = "添加关心的人请求参数")
public class AddCareUserDto implements Serializable {

    private static final long serialVersionUID = -4014707133790160742L;

    /**
     * 关心的人手机号
     */
    @Schema(description = "关心的人手机号")
    @NotEmpty(message = "关心的人手机号不能为空")
    @Pattern(regexp = "^(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-79])|(?:5[0-35-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|" +
        "(?:9[1589]))\\d{8}$", message = "联系人手机号格式不正确")
    private String carePhoneNum;

    /**
     * 昵称
     */
    @Schema(description = "昵称")
    @Length(max = 50, message = "昵称不能超过50个字")
    private String nickName;

    public String getCarePhoneNum() {
        return carePhoneNum;
    }

    public void setCarePhoneNum(String carePhoneNum) {
        this.carePhoneNum = carePhoneNum;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
