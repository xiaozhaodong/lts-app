package com.shaw.lts.handle.demain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * TrackVo
 * 用户位置信息
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/24 17:23
 **/
@Schema(title = "TrackVo", description = "位置信息")
public class TrackVo implements Serializable {

    private static final long serialVersionUID = -4685522851620220264L;

    /**
     * 经度
     */
    @Schema(description = "经度")
    private Double longitude;

    /**
     * 纬度
     */
    @Schema(description = "纬度")
    private Double latitude;

    /**
     * 海拔
     */
    @Schema(description = "海拔")
    private Double altitude;

    /**
     * 当前地址
     */
    @Schema(description = "当前地址")
    private String trackAddress;

    /**
     * 移动方向
     */
    @Schema(description = "移动方向")
    private Float direction;

    /**
     * 位置精度
     */
    @Schema(description = "位置精度")
    private Float accuracy;

    /**
     * 在此时间
     */
    @Schema(description = "在此时间")
    private String trackTime;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public String getTrackAddress() {
        return trackAddress;
    }

    public void setTrackAddress(String trackAddress) {
        this.trackAddress = trackAddress;
    }

    public Float getDirection() {
        return direction;
    }

    public void setDirection(Float direction) {
        this.direction = direction;
    }

    public Float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Float accuracy) {
        this.accuracy = accuracy;
    }

    public String getTrackTime() {
        return trackTime;
    }

    public void setTrackTime(String trackTime) {
        this.trackTime = trackTime;
    }
}
