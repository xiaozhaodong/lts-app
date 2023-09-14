package com.shaw.lts.handle.controller;

import com.shaw.lts.handle.annotation.DelegateHandler;
import com.shaw.lts.handle.demain.ApiInput;
import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.demain.BaseRespDoc;
import com.shaw.lts.handle.demain.dto.TrackInfoDto;
import com.shaw.lts.handle.enums.BusinessType;
import com.shaw.lts.handle.handler.impl.SubmitTrackHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * LtsTrackController
 * 用户位置相关
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/24 17:34
 **/
@RestController
@RequestMapping("api/track")
@Tag(name = "位置操作")
public class LtsTrackController extends BaseController {

    @Operation(summary = "上报位置信息", responses = {
        @ApiResponse(responseCode = "200", description = "请求处理成功", content =
        @Content(mediaType = "application/json", schema = @Schema(implementation = BaseRespDoc.class))),
        @ApiResponse(responseCode = "500", description = "请求处理失败", content =
        @Content(mediaType = "application/json", schema = @Schema(implementation = BaseRespDoc.class)))
    }, security = {@SecurityRequirement(name = "token")})
    @RequestMapping(value = "/submitTrack", method = RequestMethod.POST)
    @DelegateHandler(handler = SubmitTrackHandler.class, title = "上报位置信息", businessType = BusinessType.INSERT)
    public ApiOutput<Void> submitTrack(@RequestBody @Validated TrackInfoDto trackInfoDto) {
        return this.delegate(ApiInput.input(trackInfoDto));
    }
}
