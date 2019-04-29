package top.lrshuai.googlecheck.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import top.lrshuai.encryption.MDUtil;
import top.lrshuai.googlecheck.common.*;
import top.lrshuai.googlecheck.dto.GoogleDTO;
import top.lrshuai.googlecheck.dto.LoginDTO;
import top.lrshuai.googlecheck.entity.User;
import top.lrshuai.googlecheck.utils.GoogleAuthenticator;
import top.lrshuai.googlecheck.utils.Tools;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 获取缓存中的数据
     * @return
     */
    public Result getData(){
        Map<String,Object> data = new HashMap<>();
        setData(CacheKey.REGISTER_USER_KEY,data);
        setData(CacheKey.TOKEN_KEY_LOGIN_KEY,data);
        return Result.ok(data);
    }

    public void setData(String keyword,Map<String,Object> data){
        Set<String> keys = redisTemplate.keys(keyword);
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            data.put(key,redisTemplate.opsForValue().get(key));
        }
    }

    /**
     * 注册
     * @param dto
     * @return
     * @throws Exception
     */
    public Result register(LoginDTO dto) throws Exception {
        User user = new User();
        user.setUserId(Tools.getUUID());
        user.setUsername(dto.getUsername());
        user.setPassword(MDUtil.bcMD5(dto.getPassword()));
        addUser(user);
        return Result.ok();
    }


    //获取用户
    public User getUser(String username){
        User cacheUser = (User) redisTemplate.opsForValue().get(String.format(CacheKey.REGISTER_USER, username));
        return cacheUser;
    }

    //添加注册用户
    public void addUser(User user){
        if(user == null) throw new ApiException(ApiResultEnum.ERROR_NULL);
        User isRepeat = getUser(user.getUsername());
        if(isRepeat != null ){
            throw new ApiException(ApiResultEnum.USER_IS_EXIST);
        }
        redisTemplate.opsForValue().set(String.format(CacheKey.REGISTER_USER, user.getUsername()),user,1, TimeUnit.DAYS);
    }

    //更新token用户
    public void updateUser(User user,HttpServletRequest request){
        if(user == null) throw new ApiException(ApiResultEnum.ERROR_NULL);
        redisTemplate.opsForValue().set(Tools.getTokenKey(request,CacheEnum.LOGIN),user,1, TimeUnit.DAYS);
    }


    /**
     * 登录
     * @param dto
     * @return
     * @throws Exception
     */
    public Result login(LoginDTO dto) throws Exception {
        User user = getUser(dto.getUsername());
        if(user == null){
            throw new ApiException(ApiResultEnum.USER_NOT_EXIST);
        }
        if(!user.getPassword().equals(MDUtil.bcMD5(dto.getPassword()))){
            throw new ApiException(ApiResultEnum.USERNAME_OR_PASSWORD_IS_WRONG);
        }
        //随机生成token
        String token = Tools.getUUID();
        redisTemplate.opsForValue().set(String.format(CacheKey.TOKEN_KEY_LOGIN,token),user,1,TimeUnit.DAYS);
        Map<String,Object> data = new HashMap<>();
        data.put(Consts.TOKEN,token);
        return Result.ok(data);
    }

    /**
     * 生成Google 密钥
     * secret：密钥
     * secretQrCode：Google Authenticator 扫描条形码的内容
     * @param user
     * @return
     */
    public Result generateGoogleSecret(User user){
        //Google密钥
        String randomSecretKey = GoogleAuthenticator.getRandomSecretKey();
        String googleAuthenticatorBarCode = GoogleAuthenticator.getGoogleAuthenticatorBarCode(randomSecretKey, user.getUsername(), "https://www.lrshuai.top");
        Map<String,Object> data = new HashMap<>();
        //Google密钥
        data.put("secret",randomSecretKey);
        //用户二维码内容
        data.put("secretQrCode",googleAuthenticatorBarCode);
        return Result.ok(data);
    }


    /**
     * 绑定Google
     * @param dto
     * @param user
     * @return
     */
    public Result bindGoogle(GoogleDTO dto, User user, HttpServletRequest request){
        if(!StringUtils.isEmpty(user.getGoogleSecret())){
            throw new ApiException(ApiResultEnum.GOOGLE_IS_BIND);
        }
        boolean isTrue = GoogleAuthenticator.check_code(dto.getSecret(), dto.getCode(), System.currentTimeMillis());
        if(!isTrue){
            throw new ApiException(ApiResultEnum.GOOGLE_CODE_NOT_MATCH);
        }
        User cacheUser = getUser(user.getUsername());
        cacheUser.setGoogleSecret(dto.getSecret());
        updateUser(cacheUser,request);
        return Result.ok();
    }

    /**
     * Google登录
     * @param code
     * @param user
     * @return
     */
    public Result googleLogin(Long code,User user,HttpServletRequest request){
        if(StringUtils.isEmpty(user.getGoogleSecret())){
            throw new ApiException(ApiResultEnum.GOOGLE_NOT_BIND);
        }
        boolean isTrue = GoogleAuthenticator.check_code(user.getGoogleSecret(), code, System.currentTimeMillis());
        if(!isTrue){
            throw new ApiException(ApiResultEnum.GOOGLE_CODE_NOT_MATCH);
        }
        redisTemplate.opsForValue().set(Tools.getTokenKey(request,CacheEnum.GOOGLE),Consts.SUCCESS,1,TimeUnit.DAYS);
        return Result.ok();
    }


}
