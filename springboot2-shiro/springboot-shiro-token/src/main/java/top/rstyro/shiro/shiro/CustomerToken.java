package top.rstyro.shiro.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 自定义token 不在是 UsernamePasswordToken
 */
public class CustomerToken implements AuthenticationToken {

    private String token;

    public CustomerToken(String token) {
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
