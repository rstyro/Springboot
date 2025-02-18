package top.lrshuai.security.config.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import top.lrshuai.security.commons.R;
import top.lrshuai.security.config.ConfigConstValue;
import top.lrshuai.security.util.JwtUtils;
import top.lrshuai.security.util.ServletUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 这个进行token的认证拦截
 */
public class SecurityAuthTokenFilter extends BasicAuthenticationFilter {

    public SecurityAuthTokenFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        if(StringUtils.hasLength(token)){
            SecurityUser userInfo = JwtUtils.getUserInfoByToken(token);
            if(userInfo==null){
                ServletUtils.render(request,response, R.error("Token过期或无效"));
                return;
            }
            if (StringUtils.hasLength(userInfo.getUsername()) &&  SecurityContextHolder.getContext().getAuthentication() == null){
                // 如果没过期，保持登录状态
                if (!JwtUtils.isExpiration(token)){
                    // 将用户信息存入 authentication，方便后续校验
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userInfo.getUsername(), null,userInfo.getAuthorities());
                    // SecurityContextHolder 权限验证上下文
                    SecurityContext context = SecurityContextHolder.getContext();
                    // 指示用户已通过身份验证
                    context.setAuthentication(authentication);
                    System.out.println("authorite="+authentication.getAuthorities().toString());
                }
            }
        }
        // 继续下一个过滤器
        filterChain.doFilter(request, response);
    }

    /**
     * 从header或者参数中获取token
     * @return token
     */
    public String getToken(HttpServletRequest request){
        String token = request.getHeader(ConfigConstValue.TOKEN);
        if(!StringUtils.hasLength(token)){
            token=request.getParameter(ConfigConstValue.TOKEN);
        }
        return token;
    }
}
