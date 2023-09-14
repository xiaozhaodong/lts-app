package com.shaw.lts.handle.security;

import java.util.Date;

import com.shaw.lts.adapter.service.LtsSysUserService;
import com.shaw.lts.core.utils.IdGeneratorUtils;
import com.shaw.lts.handle.constant.LtsConst;
import com.shaw.lts.handle.demain.LoginUser;
import com.shaw.lts.persist.model.LtsSysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * LoginUserServiceImpl
 * UserDetailsService 服务实现
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2022/7/18 11:04
 **/
@Component
public class LoginUserServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(LoginUserServiceImpl.class);

    private final LtsSysUserService ltsSysUserService;

    public LoginUserServiceImpl(LtsSysUserService ltsSysUserService) {
        this.ltsSysUserService = ltsSysUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
        LtsSysUser ltsSysUser = ltsSysUserService.selectOne(e -> {
            e.setLoginName(loginName);
            e.setBlnDelete(LtsConst.N);
        });
        if (ltsSysUser == null) {
            logger.info("{}:新用户登录", loginName);
            String userId = IdGeneratorUtils.genId();
            ltsSysUser = installUser(loginName, userId);
            ltsSysUserService.save(ltsSysUser);
        }
        LoginUser loginUser = new LoginUser();
        BeanUtils.copyProperties(ltsSysUser, loginUser);
        return loginUser;
    }

    private LtsSysUser installUser(String loginName, String userId) {
        LtsSysUser ltsSysUser = new LtsSysUser();
        ltsSysUser.setUserId(userId);
        ltsSysUser.setLoginName(loginName);
        ltsSysUser.setBlnVip(LtsConst.N);
        ltsSysUser.setBlnEnable(LtsConst.Y);
        ltsSysUser.setBlnLock(LtsConst.N);
        ltsSysUser.setBlnDelete(LtsConst.N);
        ltsSysUser.setCreateTime(new Date());
        return ltsSysUser;
    }

}
