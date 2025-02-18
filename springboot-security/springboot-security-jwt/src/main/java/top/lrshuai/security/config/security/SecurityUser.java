package top.lrshuai.security.config.security;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class SecurityUser implements UserDetails {
    /**
     * 密码
     */
    private String password;
    /**
     * 用户名
     */
    private String username;

    /**
     * 权限
     */
    private Set<GrantedAuthority> authorities;

    /**
     * 自定义字段
     */
    private String nickName;

    public SecurityUser(String password, String username, Set<GrantedAuthority> authorities) {
        this.password = password;
        this.username = username;
        this.authorities = authorities;
    }

    public SecurityUser(String password, String username, Set<GrantedAuthority> authorities, String nickName) {
        this.password = password;
        this.username = username;
        this.authorities = authorities;
        this.nickName = nickName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * 账号是否失效
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账号是否锁定
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码是否失效
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
