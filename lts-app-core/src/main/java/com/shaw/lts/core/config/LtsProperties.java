package com.shaw.lts.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * LtsProperties
 * lts 系统配置
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/21 10:46
 **/
@Configuration
@ConfigurationProperties(prefix = "lts.sys")
public class LtsProperties {

    /**
     * 登陆url
     */
    private String loginUrl;

    /**
     * 不需要认证的url
     */
    private List<String> allowUrl;

    /**
     * jwt秘钥
     */
    private String jwtSecret;

    /**
     * jwt token过期时间 毫秒
     */
    private Long jwtExpiredTime;

    /**
     * 刷新 token 过期时间 毫秒
     */
    private Long refreshExpiredTime;

    /**
     * 短信服务地址
     */
    private String smsUrl;

    /**
     * 短信服务访问 keyId
     */
    private String smsAccessKeyId;

    /**
     * 短信服务访问密钥
     */
    private String smsAccessKeySecret;

    /**
     * 短信模板 ID
     */
    private String smsTempId;

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public List<String> getAllowUrl() {
        return allowUrl;
    }

    public void setAllowUrl(List<String> allowUrl) {
        this.allowUrl = allowUrl;
    }

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public Long getJwtExpiredTime() {
        return jwtExpiredTime;
    }

    public void setJwtExpiredTime(Long jwtExpiredTime) {
        this.jwtExpiredTime = jwtExpiredTime;
    }

    public Long getRefreshExpiredTime() {
        return refreshExpiredTime;
    }

    public void setRefreshExpiredTime(Long refreshExpiredTime) {
        this.refreshExpiredTime = refreshExpiredTime;
    }

    public String getSmsUrl() {
        return smsUrl;
    }

    public void setSmsUrl(String smsUrl) {
        this.smsUrl = smsUrl;
    }

    public String getSmsAccessKeyId() {
        return smsAccessKeyId;
    }

    public void setSmsAccessKeyId(String smsAccessKeyId) {
        this.smsAccessKeyId = smsAccessKeyId;
    }

    public String getSmsAccessKeySecret() {
        return smsAccessKeySecret;
    }

    public void setSmsAccessKeySecret(String smsAccessKeySecret) {
        this.smsAccessKeySecret = smsAccessKeySecret;
    }

    public String getSmsTempId() {
        return smsTempId;
    }

    public void setSmsTempId(String smsTempId) {
        this.smsTempId = smsTempId;
    }
}
