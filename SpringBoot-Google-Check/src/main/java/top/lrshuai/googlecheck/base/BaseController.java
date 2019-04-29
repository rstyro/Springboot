package top.lrshuai.googlecheck.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.lrshuai.googlecheck.common.ApiException;
import top.lrshuai.googlecheck.common.ApiResultEnum;
import top.lrshuai.googlecheck.common.CacheEnum;
import top.lrshuai.googlecheck.entity.User;
import top.lrshuai.googlecheck.utils.Tools;

import javax.servlet.http.HttpServletRequest;

/**
 * 基类
 */
public class BaseController {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        return request;
    }

    /**
     * 从token 中获取用户信息
     * @return
     */
    protected User getUser(){
        String tokenKey = Tools.getTokenKey(this.getRequest(), CacheEnum.LOGIN);
        User user = (User) redisTemplate.opsForValue().get(tokenKey);
        if(user == null) throw new ApiException(ApiResultEnum.AUTH_LGOIN_NOT_VALID);
        return user;
    }
}
