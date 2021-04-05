package top.rstyro.shiro.shiro.session;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import top.rstyro.shiro.utils.IdUtils;

import java.io.Serializable;

/**
 * 自定义sessionId
 * 可以搞个jwt
 */
public class ShiroSessionIdGenerator implements SessionIdGenerator {
    @Override
    public Serializable generateId(Session session) {
//        Serializable sessionId = new JavaUuidSessionIdGenerator().generateId(session);
//        return String.format("shiro_login_token_%s", sessionId);
        return IdUtils.simpleUUID();
    }
}
