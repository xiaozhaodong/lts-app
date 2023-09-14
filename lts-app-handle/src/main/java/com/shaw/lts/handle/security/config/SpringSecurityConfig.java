package com.shaw.lts.handle.security.config;


import com.shaw.lts.core.config.LtsProperties;
import com.shaw.lts.core.redis.RedisCacheService;
import com.shaw.lts.handle.security.LoginUserServiceImpl;
import com.shaw.lts.handle.security.filter.MyUsernamePasswordAuthenticationFilter;
import com.shaw.lts.handle.security.filter.TokenVerifyFilter;
import com.shaw.lts.handle.security.handler.AnonymousAuthenticationHandler;
import com.shaw.lts.handle.security.handler.CustomerAccessDeniedHandler;
import com.shaw.lts.handle.security.handler.LoginFailHandler;
import com.shaw.lts.handle.security.handler.LoginSuccessHandler;
import com.shaw.lts.handle.security.provider.DaoAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurityConfig
 * security认证配置
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/6/14 16:42
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig {

    private final LoginUserServiceImpl loginUserService;

    private final AuthenticationConfiguration authenticationConfiguration;

    private final LoginSuccessHandler loginSuccessHandler;

    private final LoginFailHandler loginFailHandler;

    private final CustomerAccessDeniedHandler customerAccessDeniedHandler;

    private final AnonymousAuthenticationHandler anonymousAuthenticationHandler;

    private final LtsProperties properties;

    private final RedisCacheService redisCacheService;

    private final TokenVerifyFilter tokenVerifyFilter;

    public SpringSecurityConfig(LoginUserServiceImpl loginUserService,
        AuthenticationConfiguration authenticationConfiguration, LoginSuccessHandler loginSuccessHandler,
        LoginFailHandler loginFailHandler, CustomerAccessDeniedHandler customerAccessDeniedHandler,
        AnonymousAuthenticationHandler anonymousAuthenticationHandler, LtsProperties properties,
        TokenVerifyFilter tokenVerifyFilter, RedisCacheService redisCacheService) {
        this.loginUserService = loginUserService;
        this.authenticationConfiguration = authenticationConfiguration;
        this.loginSuccessHandler = loginSuccessHandler;
        this.loginFailHandler = loginFailHandler;
        this.customerAccessDeniedHandler = customerAccessDeniedHandler;
        this.anonymousAuthenticationHandler = anonymousAuthenticationHandler;
        this.properties = properties;
        this.redisCacheService = redisCacheService;
        this.tokenVerifyFilter = tokenVerifyFilter;
    }

    @Bean
    public DaoAuthenticationProvider getDaoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        //        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setRedisCacheService(redisCacheService);
        daoAuthenticationProvider.setUserDetailsService(loginUserService);
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public MyUsernamePasswordAuthenticationFilter myUsernamePasswordAuthenticationFilter() throws Exception {
        MyUsernamePasswordAuthenticationFilter passwordAuthenticationFilter =
            new MyUsernamePasswordAuthenticationFilter();
        passwordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        passwordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        passwordAuthenticationFilter.setAuthenticationFailureHandler(loginFailHandler);
        return passwordAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(@NonNull HttpSecurity http) throws Exception {
        return http.formLogin().and().csrf().disable().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
            .antMatchers(properties.getAllowUrl().toArray(new String[]{})).permitAll()
            .anyRequest().authenticated().and().exceptionHandling()
            .authenticationEntryPoint(anonymousAuthenticationHandler).accessDeniedHandler(customerAccessDeniedHandler)
            .and().authenticationProvider(getDaoAuthenticationProvider())
            .addFilterBefore(tokenVerifyFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAt(myUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
