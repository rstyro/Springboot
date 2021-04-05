package top.rstyro.shiro.shiro;

import com.alibaba.druid.support.json.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import top.rstyro.shiro.commons.Consts;
import top.rstyro.shiro.commons.Result;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * 自定义shiro 过滤器
 */
@Slf4j
public class CustomerFilter extends AuthenticatingFilter {

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse){
        // 获取请求token
        String token = getRequestToken((HttpServletRequest)servletRequest);
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return new CustomerToken(token);
    }

    /**
     * 认证之前执行该方法
     * 不拦截option请求
     * 判断用户是否已经登录，
     * 如果是options的请求则放行，否则进行调用onAccessDenied进行token认证流程
     * @return boolean
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //这里只有返回false才会执行onAccessDenied方法
        if (((HttpServletRequest)request).getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }
        Subject subject = SecurityUtils.getSubject();
        return  null != subject && subject.isAuthenticated();
//        return false;
    }

    /**
     * 请求拦截
     * @param servletRequest request
     * @param servletResponse response
     * @return boolean
     * @throws Exception err
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 获取请求token，如果token不存在，直接返回401
        String token = getRequestToken((HttpServletRequest)servletRequest);
        if (StringUtils.isEmpty(token)) {
            writeOut(servletResponse,Result.error("Token 为空"));
            return false;
        }
        // 如果有，进行token验证
        try {
            SecurityUtils.getSubject().login(new CustomerToken(token));
        } catch (AuthenticationException e) {
            log.error(e.getMessage(),e);
            writeOut(servletResponse, Result.error("Token过期"));
            return false;
        }

        return true;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        try {
            writeOut(response, Result.error("Token过期或用户未登录"));
        } catch (IOException ioException) {
            log.error("onLoginFailure");
        }
        return false;
    }

    /**
     * 接口返回
     * @param servletResponse response
     * @param obj 返回的对象
     * @throws IOException err
     */
    public void writeOut(ServletResponse servletResponse,Object obj) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse)servletResponse;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.getWriter().print(JSONUtils.toJSONString(obj));
    }


    /**
     * 获取请求的token
     */
    private String getRequestToken(HttpServletRequest httpRequest) {
        // 从header中获取token
        String token = httpRequest.getHeader(Consts.TOKEN);
        // 如果header中不存在token，则从参数中获取token
        if (StringUtils.isEmpty(token)) {
            token = httpRequest.getParameter(Consts.TOKEN);
        }
        return token;
    }
}
