package com.shaw.lts.adapter.bo;

import javax.persistence.Column;
import java.io.Serializable;

/**
 * UserCareBo
 * 关心的人 bo
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/26 10:16
 **/
public class UserCareBo implements Serializable {

    private static final long serialVersionUID = 7430785734144082770L;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 关心的用户id
     */
    @Column(name = "care_user_id")
    private String careUserId;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 登录名(手机号)
     */
    @Column(name = "login_name")
    private String loginName;

    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 是否在线
     */
    @Column(name = "bln_online")
    private String blnOnline;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBlnOnline() {
        return blnOnline;
    }

    public void setBlnOnline(String blnOnline) {
        this.blnOnline = blnOnline;
    }
}
