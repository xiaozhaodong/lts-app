package com.shaw.lts.handle.demain.vo;

import java.io.Serializable;

/**
 * LoginVo
 * 登录结果
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/21 10:50
 **/
public class LoginVo implements Serializable {

    private static final long serialVersionUID = 4147854627113599806L;

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 是否 vip
     */
    private boolean blnVip;

    /**
     * vip 过期时间
     */
    private String vipExpireTime;

    /**
     * 访问 token
     */
    private String token;

    /**
     * token 过期时间
     */
    private long tokenExpire;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * 刷新token过期时间
     */
    private long refreshTokenExpire;

    /**
     * 是否在线
     */
    private boolean blnOnline;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isBlnVip() {
        return blnVip;
    }

    public void setBlnVip(boolean blnVip) {
        this.blnVip = blnVip;
    }

    public String getVipExpireTime() {
        return vipExpireTime;
    }

    public void setVipExpireTime(String vipExpireTime) {
        this.vipExpireTime = vipExpireTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTokenExpire() {
        return tokenExpire;
    }

    public void setTokenExpire(long tokenExpire) {
        this.tokenExpire = tokenExpire;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getRefreshTokenExpire() {
        return refreshTokenExpire;
    }

    public void setRefreshTokenExpire(long refreshTokenExpire) {
        this.refreshTokenExpire = refreshTokenExpire;
    }

    public boolean isBlnOnline() {
        return blnOnline;
    }

    public void setBlnOnline(boolean blnOnline) {
        this.blnOnline = blnOnline;
    }
}
