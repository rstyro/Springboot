package top.lrshuai.security.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import top.lrshuai.security.commons.R;
import top.lrshuai.security.util.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录用户没有权限访问的处理
 */
@Component
public class AccessDeny implements AccessDeniedHandler{


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        ServletUtils.render(request,response,R.error("无权访问"));
    }
}
