package com.shaw.lts.core.swagger;

import com.google.common.collect.Lists;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.BooleanSchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.JsonSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Collections;

/**
 * SwaggerConfig
 * swagger配置
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/6/20 17:53
 **/
@Configuration
public class SwaggerConfig {

    /**
     * 设置授权，登录信息
     *
     * @return OpenAPI
     */
    @Bean
    public OpenAPI customOpenApiDoc() {

        JsonSchema loginVoSchema = new JsonSchema();
        loginVoSchema.addProperty("code", new IntegerSchema().description("返回码"));
        loginVoSchema.addProperty("message", new StringSchema().description("返回消息"));
        loginVoSchema.addProperty("success", new BooleanSchema().description("成功失败标识"));
        loginVoSchema.addProperty("data", new ObjectSchema().description("响应内容(以下是)"));
        loginVoSchema.addProperty(" userId", new StringSchema().description("用户ID"));
        loginVoSchema.addProperty(" userName", new NumberSchema().description("用户名称"));
        loginVoSchema.addProperty(" blnVip", new BooleanSchema().description("是否vip"));
        loginVoSchema.addProperty(" vipExpireTime", new StringSchema().description("vip过期时间"));
        loginVoSchema.addProperty(" token", new StringSchema().description("token值"));
        loginVoSchema.addProperty(" tokenExpire", new StringSchema().description("token过期时间"));
        loginVoSchema.addProperty(" refreshToken", new StringSchema().description("刷新token"));
        loginVoSchema.addProperty(" refreshTokenExpire", new StringSchema().description("刷新token过期时间"));
        loginVoSchema.addProperty(" blnOnline", new StringSchema().description("是否在线"));


        return new OpenAPI().info(new Info()
                .title("定位寻迹后台服务API")
                .description("定位寻迹后台服务API文档")
                .version("1.0.0")
                .contact(new Contact()
                    .name("xiaozhaodong")
//                    .email("your.email@example.com")
                    .url("https://example.com")
                )
            )
//            .servers(Collections.singletonList(
//                new Server()
//                    .url("https://api.example.com")
//            ))
            .components(new Components().addSecuritySchemes("token",
                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("token").bearerFormat("JWT")))
            .paths(new Paths().addPathItem("/api/user/login", new PathItem()
                .post(new Operation().operationId("login")
                    .summary("用户登录")
                    .tags(Lists.newArrayList("Authentication"))
                    .requestBody(new RequestBody()
                        .content(new Content().addMediaType("application/x-www-form-urlencoded", new MediaType()))
                    )
                    .parameters(Arrays.asList(
                        new Parameter()
                            .in("query")
                            .name("loginName")
                            .description("手机号")
                            .required(true)
                            .schema(new Schema<>().type("string")),
                        new Parameter()
                            .in("query")
                            .name("authCode")
                            .description("短信验证码")
                            .required(true)
                            .schema(new Schema<>().type("string"))
                    ))
                    .responses(new ApiResponses()
                        .addApiResponse("200", new ApiResponse()
                            .description("Success")
                            .content(new Content().addMediaType("application/json", new MediaType()
                                .schema(loginVoSchema))))
                    )
                )
            ));

        //        return new OpenAPI()
        //            .components(new Components().addSecuritySchemes("token",
        //                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("token").bearerFormat("")));
        //            .paths(new Paths().addPathItem("/api/user/login", new PathItem()
        //                .post(new Operation().operationId("login")
        //                    .summary("用户登录")
        //                    .tags(Lists.newArrayList("Authentication"))
        //                    .requestBody(new RequestBody()
        //                        .content(new Content().addMediaType("application/x-www-form-urlencoded", new
        //                        MediaType()))
        //                    )
        //                    .parameters(Collections.singletonList(new Parameter()
        //                        .in("query")
        //                        .name("loginName")
        //                        .description("登录名称")
        //                        .required(true)
        //                        .schema(new Schema<>().type("string"))
        //                    ))
        //                    .responses(new ApiResponses()
        //                        .addApiResponse("200", new ApiResponse()
        //                            .description("Success")
        //                            .content(new Content().addMediaType("application/json", new MediaType()
        //                                .schema(loginVoSchema))))
        //                    )
        //                )
        //            ));

        //        return new OpenAPI().components(new Components().addSecuritySchemes("token",
        //                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("token").bearerFormat("JWT")))
        //            .paths(new Paths().addPathItem("/api/user/login", new PathItem()
        //                .post(new Operation().operationId("login")
        //                    .summary("用户登录")
        //                    .tags(Lists.newArrayList("Authentication"))
        //                    .responses(new ApiResponses()
        //                        .addApiResponse("200", new ApiResponse().description("Success"))))));


    }
}

//    @Bean
//    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier
//    webEndpointsSupplier,
//        ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier
//        controllerEndpointsSupplier,
//        EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties,
//        WebEndpointProperties webEndpointProperties, Environment environment) {
//        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
//        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
//        allEndpoints.addAll(webEndpoints);
//        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
//        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
//        String basePath = webEndpointProperties.getBasePath();
//        EndpointMapping endpointMapping = new EndpointMapping(basePath);
//        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment,
//            basePath);
//        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes,
//            corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath),
//            shouldRegisterLinksMapping, null);
//    }
//
//    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment
//    environment,
//        String basePath) {
//        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(
//            basePath) || ManagementPortType.get(environment).equals(
//            ManagementPortType.DIFFERENT));
//    }
