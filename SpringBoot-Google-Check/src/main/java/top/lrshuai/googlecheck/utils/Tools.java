package top.lrshuai.googlecheck.utils;

import org.springframework.util.StringUtils;
import top.lrshuai.googlecheck.common.CacheEnum;
import top.lrshuai.googlecheck.common.CacheKey;
import top.lrshuai.googlecheck.common.Consts;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 工具类
 *
 */
public class Tools {

    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
    /**
     *  获取 token
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request){
        String token = request.getHeader(Consts.TOKEN);
        if(StringUtils.isEmpty(token)){
            token = request.getParameter(Consts.TOKEN);
        }
        return token;
    }

    /**
     *  获取 tokenKey
     * @param request
     * @return
     */
    public static String getTokenKey(HttpServletRequest request, CacheEnum cacheEnum){
        String token = getToken(request);
        System.out.println("token="+token);
        if(cacheEnum.equals(CacheEnum.GOOGLE)){
            token =  String.format(CacheKey.TOKEN_KEY_GOOGLE,token);
        }else if(cacheEnum.equals(CacheEnum.LOGIN)){
            token =  String.format(CacheKey.TOKEN_KEY_LOGIN,token);
        }
        return token;
    }
}
