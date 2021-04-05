package top.rstyro.shiro.shiro.session;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import top.rstyro.shiro.commons.Consts;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Optional;

/**
 * session 从header 里面取
 *
 */
public class TokenSessionManager extends DefaultWebSessionManager {
    public TokenSessionManager() {
        super();
    }

    /**
     * 重写 getSessionId 抄一下 DefaultWebSessionManager
     * @param request
     * @param response
     * @return
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String id = WebUtils.toHttp(request).getHeader(Consts.TOKEN);
        // 如果header里面token为空，从url里面获取token
        if (!Optional.ofNullable(id).isPresent()) {
            id = WebUtils.toHttp(request).getParameter(Consts.TOKEN);
        }

        if (id != null) {
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE,ShiroHttpServletRequest.COOKIE_SESSION_ID_SOURCE);
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
            //automatically mark it valid here.  If it is invalid, the
            //onUnknownSession method below will be invoked and we'll remove the attribute at that time.
            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return id;
        }else{
            // 这里禁用掉Cookie获取方式
            return null;
        }
    }
}
