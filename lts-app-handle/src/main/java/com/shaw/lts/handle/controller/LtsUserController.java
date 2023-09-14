package com.shaw.lts.handle.controller;

import com.shaw.lts.handle.annotation.DelegateHandler;
import com.shaw.lts.handle.demain.ApiInput;
import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.demain.BaseRespDoc;
import com.shaw.lts.handle.demain.PageResult;
import com.shaw.lts.handle.demain.doc.GetCareUserDoc;
import com.shaw.lts.handle.demain.dto.AddCareUserDto;
import com.shaw.lts.handle.demain.dto.DelCareUserDto;
import com.shaw.lts.handle.demain.dto.EditCareUserDto;
import com.shaw.lts.handle.demain.dto.GetCareUserDto;
import com.shaw.lts.handle.demain.dto.GetSmsCodeDto;
import com.shaw.lts.handle.demain.vo.UserCareVo;
import com.shaw.lts.handle.enums.BusinessType;
import com.shaw.lts.handle.handler.impl.DelCareUserHandler;
import com.shaw.lts.handle.handler.impl.EditCareUserHandler;
import com.shaw.lts.handle.handler.impl.GetCareUserHandler;
import com.shaw.lts.handle.handler.impl.AddCareUserHandler;
import com.shaw.lts.handle.handler.impl.GetSmsCodeHandler;
import com.shaw.lts.handle.handler.impl.LogoutHandler;
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
 * LtsUserController
 * 用户相关controller
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/21 16:17
 **/
@RestController
@RequestMapping("api/user")
@Tag(name = "用户操作")
public class LtsUserController extends BaseController {

    @Operation(summary = "获取短信验证码", responses = {
        @ApiResponse(responseCode = "200", description = "请求处理成功", content =
        @Content(mediaType = "application/json", schema = @Schema(implementation = BaseRespDoc.class))),
        @ApiResponse(responseCode = "500", description = "请求处理失败", content =
        @Content(mediaType = "application/json", schema = @Schema(implementation = BaseRespDoc.class)))
    }, security = {@SecurityRequirement(name = "token")})
    @RequestMapping(value = "/getSmsCode", method = RequestMethod.POST)
    @DelegateHandler(handler = GetSmsCodeHandler.class, title = "获取短信验证码", businessType = BusinessType.OTHER)
    public ApiOutput<Object> getSmsCode(@RequestBody @Validated GetSmsCodeDto getSmsCodeDto) {
        return this.delegate(ApiInput.input(getSmsCodeDto));
    }

    @Operation(summary = "用户登出", responses = {
        @ApiResponse(responseCode = "200", description = "请求处理成功", content =
        @Content(mediaType = "application/json", schema = @Schema(implementation = BaseRespDoc.class))),
        @ApiResponse(responseCode = "500", description = "请求处理失败", content =
        @Content(mediaType = "application/json", schema = @Schema(implementation = BaseRespDoc.class)))
    }, security = {@SecurityRequirement(name = "token")})
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @DelegateHandler(handler = LogoutHandler.class, title = "用户登出", businessType = BusinessType.OTHER)
    public ApiOutput<Object> logout() {
        return this.delegate(ApiInput.input());
    }

    @Operation(summary = "添加关心的人", responses = {
        @ApiResponse(responseCode = "200", description = "请求处理成功", content =
        @Content(mediaType = "application/json", schema = @Schema(implementation = BaseRespDoc.class))),
        @ApiResponse(responseCode = "500", description = "请求处理失败", content =
        @Content(mediaType = "application/json", schema = @Schema(implementation = BaseRespDoc.class)))
    }, security = {@SecurityRequirement(name = "token")})
    @RequestMapping(value = "/addCareUser", method = RequestMethod.POST)
    @DelegateHandler(handler = AddCareUserHandler.class, title = "添加关心的人", businessType = BusinessType.INSERT)
    public ApiOutput<Void> addCareUser(@RequestBody @Validated AddCareUserDto addCareUserDto) {
        return this.delegate(ApiInput.input(addCareUserDto));
    }

    @Operation(summary = "获取关心的人", responses = {
        @ApiResponse(responseCode = "200", description = "请求处理成功", content =
        @Content(mediaType = "application/json", schema = @Schema(implementation = GetCareUserDoc.class))),
        @ApiResponse(responseCode = "500", description = "请求处理失败", content =
        @Content(mediaType = "application/json", schema = @Schema(implementation = BaseRespDoc.class)))
    }, security = {@SecurityRequirement(name = "token")})
    @RequestMapping(value = "/getCareUser", method = RequestMethod.POST)
    @DelegateHandler(handler = GetCareUserHandler.class, title = "获取关心的人", businessType = BusinessType.SELECT)
    public ApiOutput<PageResult<UserCareVo>> getCareUser(@RequestBody @Validated GetCareUserDto careUserDto) {
        return this.delegate(ApiInput.input(careUserDto));
    }

    @Operation(summary = "编辑关心的人", responses = {
        @ApiResponse(responseCode = "200", description = "请求处理成功", content =
        @Content(mediaType = "application/json", schema = @Schema(implementation = BaseRespDoc.class))),
        @ApiResponse(responseCode = "500", description = "请求处理失败", content =
        @Content(mediaType = "application/json", schema = @Schema(implementation = BaseRespDoc.class)))
    }, security = {@SecurityRequirement(name = "token")})
    @RequestMapping(value = "/editCareUser", method = RequestMethod.POST)
    @DelegateHandler(handler = EditCareUserHandler.class, title = "编辑关心的人", businessType = BusinessType.UPDATE)
    public ApiOutput<Void> editCareUser(@RequestBody @Validated EditCareUserDto editCareUserDto) {
        return this.delegate(ApiInput.input(editCareUserDto));
    }

    @Operation(summary = "删除关心的人", responses = {
        @ApiResponse(responseCode = "200", description = "请求处理成功", content =
        @Content(mediaType = "application/json", schema = @Schema(implementation = BaseRespDoc.class))),
        @ApiResponse(responseCode = "500", description = "请求处理失败", content =
        @Content(mediaType = "application/json", schema = @Schema(implementation = BaseRespDoc.class)))
    }, security = {@SecurityRequirement(name = "token")})
    @RequestMapping(value = "/delCareUser", method = RequestMethod.POST)
    @DelegateHandler(handler = DelCareUserHandler.class, title = "删除关心的人", businessType = BusinessType.DELETE)
    public ApiOutput<Void> delCareUser(@RequestBody @Validated DelCareUserDto  delCareUserDto) {
        return this.delegate(ApiInput.input(delCareUserDto));
    }
}
