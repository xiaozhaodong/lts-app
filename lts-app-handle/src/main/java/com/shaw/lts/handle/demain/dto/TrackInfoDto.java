package com.shaw.lts.handle.demain.dto;

import com.shaw.lts.core.valid.annotation.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * TrackInfoDto
 * 上报位置信息 dto
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/24 17:39
 **/
public class TrackInfoDto implements Serializable {

    private static final long serialVersionUID = 3491626723587217206L;

    /**
     * 经度
     */
    @Schema(description = "经度")
    @NotNull(message = "经度不能为空")
    private Double longitude;

    /**
     * 纬度
     */
    @Schema(description = "纬度")
    @NotNull(message = "纬度不能为空")
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
    @Length(max = 255, message = "当前地址不能超过 255 个字符")
    private String trackAddress;

    /**
     * 移动方向
     */
    @Schema(description = "direction")
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
    @NotEmpty(message = "在此时间不能为空")
    @Date(format = "yyyy-MM-dd HH:mm:ss", message = "时间格式不是yyyy-MM-dd HH:mm:ss")
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
