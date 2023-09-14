package com.shaw.lts.handle.handler.impl;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.shaw.lts.core.config.LtsProperties;
import com.shaw.lts.core.redis.RedisCacheService;
import com.shaw.lts.core.utils.JacksonUtils;
import com.shaw.lts.handle.demain.ApiInput;
import com.shaw.lts.handle.demain.ApiOutput;
import com.shaw.lts.handle.demain.dto.GetSmsCodeDto;
import com.shaw.lts.handle.exception.BusinessException;
import com.shaw.lts.handle.handler.AbstractApiHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * GetSmsCodeHandler
 * 获取短信验证码 handler
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/21 16:28
 **/
@Component
public class GetSmsCodeHandler extends AbstractApiHandler<GetSmsCodeDto, Object> {

    private static final Logger logger = LoggerFactory.getLogger(GetSmsCodeHandler.class);

    private final LtsProperties properties;

    private final RedisCacheService redisCacheService;

    public GetSmsCodeHandler(LtsProperties properties, RedisCacheService redisCacheService) {
        this.properties = properties;
        this.redisCacheService = redisCacheService;
    }

    @Override
    public ApiOutput<Object> handle(ApiInput<GetSmsCodeDto> input) {
        String phoneNum = input.getData().getPhoneNum();
        String phoneKey = "auth_code:" + phoneNum;
        String phoneNumVerify = "auth_verify:" + phoneNum;
        String phoneVerify = redisCacheService.get(phoneKey);
        if (StringUtils.isNotEmpty(phoneVerify)) {
            throw new BusinessException("60秒内无法重复获取");
        }
        String authCode = generateCode();
        Map<String, String> context = new HashMap<>();
        context.put("code", authCode);
        context.put("time", "5");
        try {
            Config config = new Config()
                .setAccessKeyId(properties.getSmsAccessKeyId())
                .setAccessKeySecret(properties.getSmsAccessKeySecret());
            config.endpoint = properties.getSmsUrl();
            Client client = new Client(config);
            SendSmsRequest sendSmsRequest = new SendSmsRequest().setPhoneNumbers(phoneNum).setSignName("定位助手")
                .setTemplateCode(properties.getSmsTempId()).setTemplateParam(JacksonUtils.toJson(context));
            logger.info("发送短信请求参数:{}", JacksonUtils.toJson(sendSmsRequest));
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, new RuntimeOptions());
            logger.info("发送短信返回参数:{}", JacksonUtils.toJson(sendSmsResponse));
            if ("OK".equals(sendSmsResponse.getBody().getCode())) {
                logger.info("短信发送成功");
                redisCacheService.set(phoneNumVerify, "1", 60);
                redisCacheService.set(phoneKey, authCode, 5 * 60);
                return ApiOutput.ok().message("短信发送成功");
            } else {
                logger.info("短信发送失败");
                return ApiOutput.fail().message("短信发送失败");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("调用短信服务异常");
        }
    }

    private String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int number = random.nextInt(10);
            code.append(number);
        }
        return code.toString();
    }
}
