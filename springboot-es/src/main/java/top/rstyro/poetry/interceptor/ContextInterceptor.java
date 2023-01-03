package top.rstyro.poetry.interceptor;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import top.rstyro.poetry.commons.Const;
import top.rstyro.poetry.commons.ContextVo;
import top.rstyro.poetry.util.ContextUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 上下文参数注入
 */
@Component
public class ContextInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String pageNo = request.getHeader(Const.PAGE_NO);
        String pageSize = request.getHeader(Const.PAGE_SIZE);
        String trackerId = request.getHeader(Const.TRACKER_ID);
        ContextVo contextVo = new ContextVo();
        contextVo.setTrackerId(StringUtils.hasLength(trackerId)?trackerId:IdUtil.fastSimpleUUID());
        contextVo.setPageNo(StringUtils.hasLength(pageNo)&&StrUtil.isNumeric(pageNo)?Integer.parseInt(pageNo):1);
        contextVo.setPageSize(StringUtils.hasLength(pageSize)&&StrUtil.isNumeric(pageSize)?Integer.parseInt(pageSize):10);
        ContextUtil.setVoLocal(contextVo);
        return true;
    }

}
