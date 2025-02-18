package top.rstyro.poetry.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import top.rstyro.poetry.commons.ContextVo;

/**
 * 上下文工具类
 * @author rstyro
 */
public class ContextUtil {

    private final static TransmittableThreadLocal<ContextVo> voLocal = new TransmittableThreadLocal<>();

    public static ContextVo getVoLocal(){
        ContextVo contextVo = voLocal.get();
        if(contextVo==null){
            contextVo = new ContextVo();
            voLocal.set(contextVo);
        }
        return contextVo;
    }

    public static void setVoLocal(ContextVo contextVo) {
        voLocal.set(contextVo);
    }

    public static Integer getPageNo() {
        return getVoLocal().getPageNo();
    }

    public static Integer getPageSize() {
        return getVoLocal().getPageSize();
    }

    public static String getTrackerId() {
        return getVoLocal().getTrackerId();
    }


}
