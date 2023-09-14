package com.shaw.lts.handle.demain;

import com.shaw.lts.handle.constant.LtsConst;
import com.shaw.lts.persist.model.LtsSysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * LoginUser
 * 登录用户信息
 *
 * @author xiaozhaodong
 * @version 1.0.0
 * @date 2023/7/20 20:22
 **/
public class LoginUser extends LtsSysUser implements UserDetails {

    private static final long serialVersionUID = 1432668850598385234L;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.getLoginPassword();
    }

    @Override
    public String getUsername() {
        return this.getLoginName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return LtsConst.N.equals(this.getBlnLock());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return LtsConst.Y.equals(this.getBlnEnable());
    }
}
